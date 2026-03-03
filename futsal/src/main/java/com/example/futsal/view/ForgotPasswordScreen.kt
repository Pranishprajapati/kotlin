package com.example.futsal.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.repository.UserRepoImpl
import com.example.futsal.viewmodel.UserViewModel

// Re-using the same dashing palette
val DeepBlack_F = Color(0xFF0B0E11)
val MidnightBlue_F = Color(0xFF161B22)
val NeonBlue_F = Color(0xFF00D2FF)
val ElectricCyan_F = Color(0xFF3A7BD5)
val GlassWhite_F = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val GlassBorder_F = Color(0xFFFFFFFF).copy(alpha = 0.1f)

@Composable
fun ForgotPasswordScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MidnightBlue_F, DeepBlack_F)
                )
            )
    ) {
        // Background Decoration
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopStart)
                .offset(x = (-50).dp, y = (-50).dp)
                .background(NeonBlue_F.copy(alpha = 0.1f), CircleShape)
                .blur(80.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Reset Password",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Enter your email to receive a reset link",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Surface(
                color = GlassWhite_F,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, GlassBorder_F),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address", color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonBlue_F) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonBlue_F,
                            unfocusedBorderColor = GlassBorder_F,
                            cursorColor = NeonBlue_F
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Reset Button
                    Button(
                        onClick = {
                            if (email.isBlank()) {
                                Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            isLoading = true
                            userViewModel.forgotPassword(email) { success, message ->
                                isLoading = false
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                if (success) onNavigate("login")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = if (isLoading) listOf(Color.Gray, Color.DarkGray) else listOf(ElectricCyan_F, NeonBlue_F)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("SEND RESET LINK", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { if (!isLoading) onNavigate("login") }) {
                Text("Back to Login", color = NeonBlue_F, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
