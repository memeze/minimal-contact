package com.bemily.samplecontact.util

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds
import android.provider.ContactsContract.Data
import android.util.Log
import android.util.SparseArray
import com.bemily.samplecontact.data.model.Contact

class ContactHelper(private val context: Context) {
    fun getContacts(): List<Contact> {
        val contacts = SparseArray<Contact>()

        val uri = CommonDataKinds.Phone.CONTENT_URI
        val projection = getContactProjection()
        val selection = null
        val selectionArgs = null
        val sortOrder = "${CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        try {
            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
                while (cursor.moveToNext()) {
                    val idIndex = cursor.getColumnIndex(Data.RAW_CONTACT_ID)
                    val nameIndex = cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME)

                    val id = cursor.getInt(idIndex)
                    val name = cursor.getString(nameIndex)
                    val phoneNumbers = listOf<String>()
                    val contact = Contact(id, name, phoneNumbers)

                    contacts.put(id, contact)
                }
            }
        } catch (e: Exception) {
            Log.e("CONTACT", "getContacts : $e")
        }

        val phoneNumbers = getPhoneNumbers()
        for (i in 0 until phoneNumbers.size()) {
            val key = phoneNumbers.keyAt(i)
            contacts[key]?.phoneNumbers = phoneNumbers.valueAt(i)
        }

        val result = ArrayList<Contact>(contacts.size())
        (0 until contacts.size()).mapTo(result) {
            contacts.valueAt(it)
        }

        return result.sortedBy { contact -> contact.name }
    }

    private fun getPhoneNumbers(): SparseArray<ArrayList<String>> {
        val phoneNumbers = SparseArray<ArrayList<String>>()
        val uri = CommonDataKinds.Phone.CONTENT_URI
        val projection = getPhoneNumberProjection()
        try {
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                while (cursor.moveToNext()) {
                    val idIndex = cursor.getColumnIndex(Data.RAW_CONTACT_ID)
                    val id = cursor.getInt(idIndex)

                    val phoneNumberIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)
                    val phoneNumber = cursor.getString(phoneNumberIndex)

                    if (phoneNumbers[id] == null) {
                        phoneNumbers.put(id, ArrayList())
                    }
                    phoneNumbers[id].add(phoneNumber)
                }
            }
        } catch (e: Exception) {
            Log.e("CONTACT", "getPhoneNumbers : $e")
        }

        return phoneNumbers
    }

    fun getContactProjection() = arrayOf(
        Data.RAW_CONTACT_ID,
        CommonDataKinds.Phone.DISPLAY_NAME
    )

    fun getPhoneNumberProjection() = arrayOf(
        Data.RAW_CONTACT_ID,
        CommonDataKinds.Phone.NUMBER
    )
}