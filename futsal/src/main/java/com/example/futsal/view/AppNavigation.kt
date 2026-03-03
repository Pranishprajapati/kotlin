package com.example.futsal.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // ---------------- SPLASH ----------------
        composable("splash") {
            SplashScreen(
                onNavigate = {
                    // Always go to welcome first
                    navController.navigate("welcome") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // ---------------- WELCOME ----------------
        composable("welcome") {
            WelcomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        // ---------------- LOGIN ----------------
        composable("login") {
            LoginScreen(
                onNavigate = { route ->
                    // After successful login, fetch role
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let { user ->
                        val dbRef = FirebaseDatabase.getInstance().reference
                            .child("users").child(user.uid).child("role")
                        dbRef.get().addOnSuccessListener { snapshot ->
                            val role = snapshot.getValue(String::class.java)
                            if (role == "admin") {
                                navController.navigate("admin") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }.addOnFailureListener {
                            // fallback to main if role fetch fails
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // ---------------- REGISTER ----------------
        composable("register") {
            RegisterScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("register") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ---------------- FORGOT PASSWORD ----------------
        composable("forgot") {
            ForgotPasswordScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // ---------------- MAIN USER SCREEN ----------------
        composable("main") {
            MainScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("welcome") { popUpTo("main") { inclusive = true } }
                }
            )
        }

        // ---------------- ADMIN SCREEN ----------------
        composable("admin") {
            AdminMainScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("welcome") { popUpTo("admin") { inclusive = true } }
                }
            )
        }
    }
}