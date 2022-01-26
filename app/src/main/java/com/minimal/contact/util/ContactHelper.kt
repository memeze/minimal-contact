package com.minimal.contact.util

import android.content.ContentResolver
import com.minimal.contact.data.model.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactHelper @Inject constructor(
    private val contentResolver: ContentResolver,
    private val contactQuery: ContactQuery
) {
    fun fetchContacts(): Flow<List<Contact>> {
        return contactQuery.contactQueryFlow(contentResolver)
    }

    companion object {
        private val TAG = ContactHelper::class.java.simpleName
    }
}