package com.example.futsal.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.model.UserModel
import com.example.futsal.repository.UserRepoImpl
import com.example.futsal.viewmodel.UserViewModel

// Dashing Theme Colors
val DeepBlack_Reg = Color(0xFF0B0E11)
val MidnightBlue_Reg = Color(0xFF161B22)
val NeonBlue_Reg = Color(0xFF00D2FF)
val ElectricCyan_Reg = Color(0xFF3A7BD5)
val GlassWhite_Reg = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val GlassBorder_Reg = Color(0xFFFFFFFF).copy(alpha = 0.1f)

@Composable
fun RegisterScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val scrollState = rememberScrollState()

    // State for required fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(MidnightBlue_Reg, DeepBlack_Reg)))
    ) {
        // Decorative background glow
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-80).dp, y = 80.dp)
                .background(NeonBlue_Reg.copy(alpha = 0.1f), CircleShape)
                .blur(100.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(text = "Join Futsilo", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
            Text(
                text = "Create your booking profile", 
                color = Color.White.copy(alpha = 0.5f), 
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Surface(
                color = GlassWhite_Reg,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, GlassBorder_Reg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    DashingInput_Reg(fullName, { fullName = it }, "Full Name", Icons.Default.Person)
                    Spacer(modifier = Modifier.height(16.dp))
                    DashingInput_Reg(email, { email = it }, "Email Address", Icons.Default.Email)
                    Spacer(modifier = Modifier.height(16.dp))
                    DashingInput_Reg(phoneNumber, { phoneNumber = it }, "Phone Number", Icons.Default.Phone)
                    Spacer(modifier = Modifier.height(16.dp))
                    DashingInput_Reg(city, { city = it }, "City", Icons.Default.LocationOn)
                    Spacer(modifier = Modifier.height(16.dp))
                    DashingInput_Reg(address, { address = it }, "Full Address", Icons.Default.Home)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Gender Selection
                    Text("Gender", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Male", "Female", "Others").forEach { option ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { gender = option }
                                    .background(if (gender == option) NeonBlue_Reg.copy(alpha = 0.15f) else Color.Transparent, RoundedCornerShape(12.dp))
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                RadioButton(
                                    selected = (gender == option),
                                    onClick = { gender = option },
                                    colors = RadioButtonDefaults.colors(selectedColor = NeonBlue_Reg, unselectedColor = Color.White.copy(alpha = 0.4f))
                                )
                                Text(option, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    DashingInput_Reg(password, { password = it }, "Password", Icons.Default.Lock, true)
                    Spacer(modifier = Modifier.height(16.dp))
                    DashingInput_Reg(confirmPassword, { confirmPassword = it }, "Confirm Password", Icons.Default.Lock, true)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Register Button
                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
                                Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (password != confirmPassword) {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            
                            val user = UserModel(
                                name = fullName, email = email, phoneNumber = phoneNumber,
                                city = city, address = address, gender = gender
                            )
                            
                            userViewModel.register(user, password) { success, message ->
                                if (success) {
                                    Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_LONG).show()
                                    onNavigate("login")
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                Brush.horizontalGradient(listOf(ElectricCyan_Reg, NeonBlue_Reg))
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("REGISTER NOW", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 32.dp)) {
                Text("Already a member? ", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                TextButton(onClick = { onNavigate("login") }) {
                    Text("Sign In", color = NeonBlue_Reg, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DashingInput_Reg(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.6f)) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(icon, contentDescription = null, tint = NeonBlue_Reg) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonBlue_Reg,
            unfocusedBorderColor = GlassBorder_Reg,
            cursorColor = NeonBlue_Reg
        ),
        shape = RoundedCornerShape(12.dp)
    )
}
