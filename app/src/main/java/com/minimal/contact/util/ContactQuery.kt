package com.minimal.contact.util

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract.Contacts
import com.minimal.contact.data.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContactQuery {
    fun contactQueryFlow(contentResolver: ContentResolver): Flow<List<Contact>> {
        return contentResolver.queryFlow(
            uri = Contacts.CONTENT_URI,
            projection = projection,
            sortOrder = Contacts.SORT_KEY_PRIMARY
        ).map {
            it.mapEachRow { cursor ->
                Contact(
                    id = getContactId(cursor),
                    lookupKey = getLookupKey(cursor),
                    displayName = getDisplayName(cursor),
                    phoneNumber = ""
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
}