package com.example.phone_book.services

import androidx.compose.runtime.mutableStateListOf
import com.example.phone_book.model.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object ContactService {
    val contacts = mutableStateListOf<Contact>()

    init {
        getContacts()
    }

    fun getContacts() = CoroutineScope(Dispatchers.IO).launch {
        Firebase.firestore.collection("contacts").get().addOnSuccessListener {
            contacts.clear()
            for (document in it) {
                contacts.add(Contact.fromSnapshot(document))
            }
        }
    }

    fun createContact(name: String, note: String, phoneNumber: String) = CoroutineScope(Dispatchers.IO).launch {
        val contact = Contact(name = name, phoneNumber = phoneNumber, note = note);

        Firebase.firestore.collection("contacts").add(object {
            val name = contact.name
            val note = contact.note
            val phoneNumber = contact.phoneNumber
        }).addOnSuccessListener {
            contact.id = it.id;
            contacts.add(contact);
        }
    }

    fun removeContact(contact: Contact) = CoroutineScope(Dispatchers.IO).launch {
        Firebase.firestore.collection("contacts").document(contact.id).delete()
            .addOnSuccessListener {
                contacts.remove(contact)
            }
    }

    fun editContact(name: String, note: String, phoneNumber: String, id: String) = CoroutineScope(Dispatchers.IO).launch {
        Firebase.firestore.collection("contacts").document(id).set(object {
            val name = name
            val note = note
            val phoneNumber = phoneNumber
        }).addOnSuccessListener {
            getContacts()
        }
    }
}