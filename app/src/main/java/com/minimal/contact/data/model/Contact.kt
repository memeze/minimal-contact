package com.minimal.contact.data.model

data class Contact(
    val id: Long,
    val lookupKey: String?,
    val displayName: String?,
    var phones: List<Phone> = listOf()
)

data class Phone(
    val id: Long,
    val number: String
)
