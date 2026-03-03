package com.example.futsal.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.repository.UserRepoImpl
import com.example.futsal.viewmodel.UserViewModel

@Composable
fun LoginScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MidnightBlue, DeepBlack)
                )
            )
    ) {
        // Background Decoration
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .background(NeonBlue.copy(alpha = 0.1f), CircleShape)
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
                text = "Welcome Back",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Sign in to continue your journey",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Surface(
                color = GlassWhite,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, GlassBorder),
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
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonBlue) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = GlassBorder,
                            cursorColor = NeonBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonBlue) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.5f)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = GlassBorder,
                            cursorColor = NeonBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { onNavigate("forgot") },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Forgot Password?", color = NeonBlue, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    Button(
                        onClick = {
                            userViewModel.login(email, password) { success, message, isAdmin ->
                                if (success) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    if (isAdmin) {
                                        onNavigate("admin")   // Go to AdminScreen
                                    } else {
                                        onNavigate("main")    // Normal HomeScreen
                                    }
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(ElectricCyan, NeonBlue)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("LOGIN", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                TextButton(onClick = { onNavigate("register") }) {
                    Text("Register Now", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
