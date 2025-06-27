package com.example.jijipay.Interface

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.*
import kotlinx.coroutines.*

@Composable
fun AuthScreen(
    navController: NavController,
    onAuthSuccess: () -> Unit
) {
    // State
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("tenant") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(value = email, label = "Email") { email = it }
        Spacer(modifier = Modifier.height(8.dp))
        AuthTextField(
            value = password,
            label = "Password",
            isPassword = true
        ) { password = it }
        Spacer(modifier = Modifier.height(8.dp))
        AuthTextField(value = name, label = "Name") { name = it }
        Spacer(modifier = Modifier.height(8.dp))

        RoleSelector(role = role, onRoleSelected = { role = it })

        if (error.isNotEmpty()) {
            Text(text = error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank() || name.isBlank()) {
                    error = "All fields are required here"
                    return@Button
                }

                isLoading = true
                error = ""

                coroutineScope.launch {
                    try {
                        withTimeout(10000) { // 10-second timeout
                            handleRegistration(
                                auth = auth,
                                email = email,
                                password = password,
                                role = role,
                                onSuccess = {
                                    Log.d("RegisterScreen", "Registration successful for email: $email")
                                    onAuthSuccess()
                                    try {
                                        navController.navigate("login/$email/$role") {
                                            popUpTo("register") { inclusive = true }
                                        }
                                    } catch (e: Exception) {
                                        error = "Navigation failed: ${e.message}"
                                        Log.e("RegisterScreen", "Navigation error", e)
                                    }
                                    isLoading = false
                                },
                                onError = {
                                    error = it
                                    isLoading = false
                                    Log.e("RegisterScreen", "Registration error: $it")
                                }
                            )
                        }
                    } catch (e: TimeoutCancellationException) {
                        error = "Registration timed out. Check your internet connection."
                        isLoading = false
                        Log.e("RegisterScreen", "Registration timeout", e)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Register")
            }
        }
    }
}

@Composable
fun LoginScreen(
    navController: NavController,

) {
    // State
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = email,
            label = "Email",
            enabled = true // Email is pre-filled and non-editable
        ) { email = it }
        Spacer(modifier = Modifier.height(8.dp))
        AuthTextField(
            value = password,
            label = "Password",
            isPassword = true
        ) { password = it }
        Spacer(modifier = Modifier.height(8.dp))

        if (error.isNotEmpty()) {
            Text(text = error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                if (password.isBlank()) {
                    error = "Password is required"
                    return@Button
                }

                isLoading = true
                error = ""

                coroutineScope.launch {
                    try {
                        withTimeout(10000) { // 10-second timeout
                            handleLogin(
                                auth = auth,
                                email = email,
                                password = password,
                                onSuccess = {
                                    navController.navigate("selectiondash")
                                    Log.d("LoginScreen", "Login successful for email: $email")
//                                    onAuthSuccess(role)
//                                    try {
//                                        navController.navigate(if (role == "biller") "biller_dashboard" else "tenant_dashboard") {
//                                            popUpTo("login/$email/$role") { inclusive = true }
//                                        }
//                                    } catch (e: Exception) {
//                                        error = "Navigation failed: ${e.message}"
//                                        Log.e("LoginScreen", "Navigation error", e)
//                                    }
                                    isLoading = false
                                },
                                onError = {
                                    error = it
                                    isLoading = false
                                    Log.e("LoginScreen", "Login error: $it")
                                }
                            )
                        }
                    } catch (e: TimeoutCancellationException) {
                        error = "Login timed out. Check your internet connection."
                        isLoading = false
                        Log.e("LoginScreen", "Login timeout", e)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Login")
            }
        }
    }
}

@Composable
fun AuthTextField(
    value: String,
    label: String,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email)
    )
}

@Composable
fun RoleSelector(role: String, onRoleSelected: (String) -> Unit) {
    Row {
        listOf("tenant", "biller").forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = role == option, onClick = { onRoleSelected(option) })
                Text(option.replaceFirstChar { it.uppercase() })
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

private fun handleRegistration(
    auth: FirebaseAuth,
    email: String,
    password: String,
    role: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                val errorMsg = task.exception?.message ?: "Registration failed"
                onError(errorMsg)
            }
        }
}

private fun handleLogin(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                val errorMsg = task.exception?.message ?: "Login failed"
                onError(errorMsg)
            }
        }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    val navController = rememberNavController()
    AuthScreen(
        navController = navController,
        onAuthSuccess = TODO()
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}