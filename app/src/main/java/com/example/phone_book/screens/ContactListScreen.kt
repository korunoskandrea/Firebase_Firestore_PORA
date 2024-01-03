@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.phone_book.model.Contact
import com.example.phone_book.navigation.Screen
import com.example.phone_book.services.ContactService

@Composable
fun ContactListScreen(title: String, navController: NavController) {
    var showKeyPad by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    var newContactName by remember { mutableStateOf("") }
    var newContactNote by remember { mutableStateOf("") }
    var newContactPhoneNumber by remember { mutableStateOf("") }
    var contactId by remember { mutableStateOf("") }

    BaseScaffoldLayout(
        title = title,
        actions = {
            IconButton(onClick = {
                showKeyPad = true
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add phone number")
            }
        },
        content = {
            LazyColumn {
                items(ContactService.contacts) {
                    ContactTile(contact = it) {
                        editMode = true
                        showKeyPad = true
                        newContactName = it.name
                        newContactNote = it.note
                        newContactPhoneNumber = it.phoneNumber
                        contactId = it.id
                    }
                }
            }
            if (showKeyPad) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showKeyPad = false
                        editMode = false
                        newContactName = ""
                        newContactNote = ""
                        newContactPhoneNumber = ""
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, bottom = 50.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomTextInput(
                            label = "Name",
                            value = newContactName,
                            onValueChange = { newContactName = it })
                        CustomTextInput(
                            label = "Note",
                            value = newContactNote,
                            onValueChange = { newContactNote = it })
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = newContactPhoneNumber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                        )
                        PhoneNumPad(onItemClicked = {
                            if (it != "<") {
                                newContactPhoneNumber += it
                            } else if (newContactPhoneNumber.isNotEmpty()) {
                                newContactPhoneNumber = newContactPhoneNumber.substring(
                                    0,
                                    newContactPhoneNumber.lastIndex
                                )
                            }
                        })
                        ElevatedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (newContactName.trim()
                                        .isNotEmpty() && newContactPhoneNumber.isNotEmpty()
                                ) MaterialTheme.colorScheme.inversePrimary else Color.LightGray,
                            ),
                            onClick = {
                                if (newContactName.trim()
                                        .isNotEmpty() && newContactPhoneNumber.isNotEmpty()
                                ) {
                                    if (editMode) {
                                        ContactService.editContact(
                                            newContactName,
                                            newContactNote,
                                            newContactPhoneNumber,
                                            contactId
                                        )
                                    } else {
                                        ContactService.createContact(
                                            newContactName,
                                            newContactNote,
                                            newContactPhoneNumber
                                        )
                                    }

                                    showKeyPad = false
                                    editMode = false
                                    newContactName = ""
                                    newContactNote = ""
                                    newContactPhoneNumber = ""
                                }
                            }
                        ) {
                            val buttonText = if (!editMode) "Add contact" else "Save changes"
                            Text(text = buttonText, color = Color.Black)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ContactTile(contact: Contact, onLongClick: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .combinedClickable(
                onClick = { expanded = !expanded },
                onLongClick = onLongClick
            ),
        shape = if (!expanded) RoundedCornerShape(5.dp) else RoundedCornerShape(10.dp),
    ) {
        Row(
            Modifier
                .padding(all = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Rounded.AccountCircle,
                    Icons.Rounded.AccountCircle.name,
                    Modifier
                        .size(35.dp)
                        .padding(end = 10.dp)
                )
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                if (contact.note.trim() != "") {
                    Text(
                        text = contact.note,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 10.dp),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            IconButton(
                onClick = { ContactService.removeContact(contact) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Icon(Icons.Rounded.Delete, "Delete contact")
            }
        }
        if (expanded) {
            Column(
                modifier = Modifier.padding(start = 40.dp)
            ) {
                Text(
                    text = "Mobile: ${contact.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun CustomTextInput(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        onValueChange = onValueChange,
        value = value,
        placeholder = { Text(label) },
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PhoneNumPad(onItemClicked: (String) -> Unit) {
    val numPad = listOf<List<String>>(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("<", "0", "#"),
    )

    Column {
        for (row in numPad) {
            Row {
                for (symbol in row) {
                    ElevatedButton(
                        onClick = { onItemClicked(symbol) },
                        shape = CircleShape,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 5.dp),
                            text = symbol,
                            fontSize = 20.sp,
                        )
                    }
                }
            }
        }
    }
}