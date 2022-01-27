package com.minimal.contact.util

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Contacts
import android.provider.ContactsContract.Data
import com.minimal.contact.data.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.minimal.contact.data.model.Phone as PhoneModel

class ContactQuery {
    fun contactQueryFlow(contentResolver: ContentResolver): Flow<List<Contact>> {
        return contentResolver.runQueryFlow(
            uri = Contacts.CONTENT_URI,
            projection = projection,
            sortOrder = Contacts.SORT_KEY_PRIMARY
        ).map {
            it.mapEachRow { cursor ->
                Contact(
                    id = getContactId(cursor),
                    lookupKey = getLookupKey(cursor),
                    displayName = getDisplayName(cursor)
                )
            }
        }.map { contacts ->
            contacts.map { contact ->
                var phones = listOf<PhoneModel>()

                contentResolver.runQuery(
                    uri = Data.CONTENT_URI,
                    selection = "${Data.CONTACT_ID} = ${contact.id} AND (${Data.MIMETYPE} IN ?)",
                    selectionArgs = arrayOf(Phone.CONTENT_ITEM_TYPE)
                ).iterate { cursor ->
                    phones = getPhoneNumbers(cursor)
                }

                Contact(
                    id = contact.id,
                    lookupKey = contact.lookupKey,
                    displayName = contact.displayName,
                    phones = phones.toList()
                )
            }
        }
    }

    private val projection = arrayOf(
        Contacts._ID,
        Contacts.DISPLAY_NAME_PRIMARY,
        Contacts.STARRED,
        Contacts.LOOKUP_KEY
    )

    private fun getContactId(cursor: Cursor): Long {
        val index = cursor.getColumnIndex(Contacts._ID)
        return cursor.getLong(index)
    }

    private fun getLookupKey(cursor: Cursor): String? {
        val index = cursor.getColumnIndex(Contacts.LOOKUP_KEY)
        return cursor.getString(index)
    }

    private fun getDisplayName(cursor: Cursor): String? {
        val index = cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY)
        return cursor.getString(index)
    }

    private fun getPhoneNumbers(cursor: Cursor): List<com.minimal.contact.data.model.Phone> {
        val phones = mutableListOf<com.minimal.contact.data.model.Phone>()
        val phoneNumber = cursor[Phone.NUMBER]
        val phoneId = cursor[Phone._ID].toLongOrNull()
        if (phoneNumber.isNotBlank() && phoneId != null) {
            phones.add(com.minimal.contact.data.model.Phone(phoneId, phoneNumber))
        }
        return phones
    }
}