package com.example.futsal.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.model.UserModel
import com.example.futsal.repository.UserRepoImpl
import com.example.futsal.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.text.ifEmpty

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    var userProfile by remember { mutableStateOf<UserModel?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            userViewModel.getUserData(user.uid) { model ->
                model?.let {
                    userProfile = it
                    name = it.name
                    phone = it.phoneNumber
                    city = it.city
                    address = it.address
                    gender = it.gender
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(32.dp))
        Surface(modifier = Modifier.size(120.dp), shape = CircleShape, color = NeonBlue_Main.copy(alpha = 0.2f), border = BorderStroke(2.dp, NeonBlue_Main)) {
            Icon(Icons.Default.Person, null, tint = NeonBlue_Main, modifier = Modifier.padding(24.dp))
        }
        Text(name.ifEmpty { "User Name" }, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 16.dp))
        Text(userProfile?.email ?: "", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Surface(color = GlassWhite_Main, shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, GlassBorder_Main), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Personal Details", color = NeonBlue_Main, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = {
                        if (isEditing) {
                            val updatedUser = userProfile?.copy(name = name, phoneNumber = phone, city = city, address = address, gender = gender)
                            updatedUser?.let { userViewModel.updateUserData(it) { success, _ -> if (success) isEditing = false } }
                        } else isEditing = true
                    }) { Icon(if (isEditing) Icons.Default.Save else Icons.Default.Edit, null, tint = NeonBlue_Main) }
                }
                ProfileField("Full Name", name, isEditing, { name = it }, Icons.Default.Badge)
                ProfileField("Phone Number", phone, isEditing, { phone = it }, Icons.Default.Phone)
                ProfileField("City", city, isEditing, { city = it }, Icons.Default.LocationCity)
                ProfileField("Full Address", address, isEditing, { address = it }, Icons.Default.HomeWork)
                ProfileField("Gender", gender, isEditing, { gender = it }, Icons.Default.Transgender)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), border = BorderStroke(1.dp, Color.Red.copy(0.5f))) {
            Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.Red)
            Spacer(modifier = Modifier.width(8.dp))
            Text("LOGOUT", color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileField(label: String, value: String, isEditing: Boolean, onValueChange: (String) -> Unit, icon: ImageVector) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = NeonBlue_Main.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
        }
        if (isEditing) {
            TextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, cursorColor = NeonBlue_Main, focusedIndicatorColor = NeonBlue_Main))
        } else {
            Text(value.ifEmpty { "Not specified" }, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 26.dp, top = 4.dp))
            HorizontalDivider(color = GlassBorder_Main, modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp)
        }
    }
}