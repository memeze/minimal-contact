package com.bemily.samplecontact.data.model

import java.util.*

data class Contact(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val phoneNumber: String
) {
    companion object {
        fun getMockContactList(): List<Contact> {
            return listOf(
                Contact(name = "피카츄", phoneNumber = "010-0000-0000"),
                Contact(name = "라이츄", phoneNumber = "010-0000-0000"),
                Contact(name = "파이리", phoneNumber = "010-0000-0000"),
                Contact(name = "꼬부기", phoneNumber = "010-0000-0000"),
                Contact(name = "버터플", phoneNumber = "010-0000-0000"),
                Contact(name = "야도란", phoneNumber = "010-0000-0000"),
                Contact(name = "피죤투", phoneNumber = "010-0000-0000"),
                Contact(name = "또가스", phoneNumber = "010-0000-0000")
            )
        }
    }
}
