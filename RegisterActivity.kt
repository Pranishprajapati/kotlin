package com.example.pranish

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pranish.model.UserModel
import com.example.pranish.repository.UserRepoImpl
import com.example.pranish.ui.theme.Blue
import com.example.pranish.ui.theme.light
import com.example.pranish.ui.theme.white
import com.example.pranish.viewmodel.UserViewModel
import java.util.Calendar

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterBody()
        }
    }
}

@Composable
fun RegisterBody() {
    val userViewModel = remember { UserViewModel(repo = UserRepoImpl()) }
    val context = LocalContext.current
    val activity = (context as? ComponentActivity)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var terms by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate by remember { mutableStateOf("") }

    val datepicker = DatePickerDialog(
        context, { _, y, m, d -> selectedDate = "$y/${m+1}/$d" },
        year, month, day
    )

    val sharedPreference = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(white)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                "Register",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Blue,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("abc@gmail.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = light,
                    unfocusedContainerColor = light,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.baseline_visibility_24)
                            else
                                painterResource(R.drawable.baseline_visibility_off_24),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("********") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = light,
                    unfocusedContainerColor = light,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Date of Birth Field
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("dd/mm/yyyy") },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datepicker.show() }
                    .padding(horizontal = 15.dp),
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = light,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = light,
                    unfocusedContainerColor = light,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Terms Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = terms,
                    onCheckedChange = { terms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Blue,
                        checkmarkColor = white
                    )
                )
                Text("I agree to terms & conditions")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Register Button
            Button(
                onClick = {
                    if (!terms) {
                        Toast.makeText(context, "Please agree to terms & conditions", Toast.LENGTH_SHORT).show()
                    } else {
                        editor.putString("email", email)
                        editor.putString("password", password)
                        editor.putString("date", selectedDate)
                        editor.apply()

                        userViewModel.register(email, password) { success, msg, userId ->
                            if (success) {
                                val model = UserModel(
                                    email = email,
                                    userId = userId,
                                    firstName = "",
                                    lastName = "",
                                    dob = selectedDate
                                )
                                userViewModel.addUserToDatabase(userId, model) { successDb, msgDb ->
                                    if (successDb) {
                                        Toast.makeText(context, msgDb, Toast.LENGTH_SHORT).show()
                                        activity?.finish() // close registration
                                    } else {
                                        Toast.makeText(context, msgDb, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Blue)
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Navigate to Login
            Text(buildAnnotatedString {
                append("Already a member?")
                withStyle(SpanStyle(color = Blue)) { append(" Sign In") }
            },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .clickable {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        activity?.finish()
                    })
        }
    }
}
