package com.minimal.contact.data.model

data class Contact(
    val id: Long,
    val lookupKey: String?,
    val displayName: String?,
    var phoneNumber: String
)
