package com.bemily.samplecontact.util

import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Data
import android.util.Log
import com.bemily.samplecontact.data.model.Contact
import com.bemily.samplecontact.data.model.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun fetchContacts(lastUpdateTime: Long): Flow<Result<List<Contact>>> {
        return flow {
            try {
                emit(Result.Loading)
                emit(Result.Success(getContacts(lastUpdateTime)))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }

    /**
     * 현재 Phone number 기준으로 가져오기 때문에,
     * 같은 연락처의 여러 전화번호가 다른 item 으로 return 됩니다.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getContacts(lastUpdateTime: Long): MutableList<Contact> {
        Log.d(TAG, "getContacts()")
        val contactList = mutableListOf<Contact>()

        val uri = Phone.CONTENT_URI
        val projection = getContactProjection()
        val selection = "${Phone.CONTACT_LAST_UPDATED_TIMESTAMP} > ?"
        val selectionArgs = arrayOf(lastUpdateTime.toString())
        val sortOrder = "${Phone.DISPLAY_NAME} ASC"

        try {
            context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
                while (cursor.moveToNext()) {
                    val idIndex = cursor.getColumnIndex(Phone.CONTACT_ID)
                    val nameIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME)
                    val numberIndex = cursor.getColumnIndex(Phone.NUMBER)

                    val id = cursor.getInt(idIndex)
                    val name = cursor.getString(nameIndex)
                    val phoneNumber = cursor.getString(numberIndex)

                    val contact = Contact(id, name, phoneNumber)
                    contactList.add(contact)
                }

                cursor.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getContacts : $e")
        }

        // deleted list get start
        val deletedList = mutableListOf<Int>()

        try {
            context.contentResolver.query(
                ContactsContract.DeletedContacts.CONTENT_URI,
                arrayOf(
                    ContactsContract.DeletedContacts.CONTACT_ID,
                    ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP
                ),
                "${ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP} > ?",
                arrayOf(lastUpdateTime.toString()),
                null
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val idIndex = cursor.getColumnIndex(ContactsContract.DeletedContacts.CONTACT_ID)
                    val id = cursor.getInt(idIndex)

                    deletedList.add(id)
                }

                cursor.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getContacts : $e")
        }

        Log.e(":::::", "deletedList == $deletedList")

        // deleted list get end

        return contactList
    }

    private fun getContactProjection() = arrayOf(
        Phone.CONTACT_ID,
        Phone.DISPLAY_NAME,
        Phone.NUMBER
    )

    companion object {
        private const val TAG = "ContactHelper"
    }
}