package com.example.phone_book.model

import android.media.Image
import com.google.firebase.firestore.QueryDocumentSnapshot

class Contact(
    var name: String,
    var phoneNumber: String,
    var note: String = "",
    var id: String  = "",
) {
    companion object {
        fun fromSnapshot(document: QueryDocumentSnapshot): Contact {
            return Contact(
                id = document.id,
                name = document.data["name"].toString(),
                phoneNumber = document.data["phoneNumber"].toString(),
                note = document.data["note"].toString()
            )
        }
    }
}