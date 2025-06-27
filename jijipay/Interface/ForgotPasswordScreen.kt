package com.example.myapplication.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


//@Composable
//fun ForgotPasswordScreen(navController :NavController,
//                         onResetSuccess: () -> Unit = {}
//) {
//    var email by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf("") }
//    var isSubmitted by remember { mutableStateOf(false) }
//    var isLoading by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (isSubmitted) {
//            Icon(
//                imageVector = Icons.Filled.Email,
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.size(64.dp)
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "Check your email",
//                style = MaterialTheme.typography.titleLarge
//            )
//            Text(
//                text = "A password reset link has been sent to $email.",
//                style = MaterialTheme.typography.bodyMedium,
//                textAlign = TextAlign.Center
//            )
//        } else {
//            Text(
//                text = "Forgot Password",
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(bottom = 24.dp)
//            )
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = {
//                    email = it
//                    errorMessage = ""
//                },
//                label = { Text(text = "Email Address") },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Email
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            if (errorMessage.isNotEmpty()) {
//                Text(
//                    text = errorMessage,
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(top = 4.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = {
//                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                        errorMessage = "Please enter a valid email address."
//                        return@Button
//                    }
//
//                    isLoading = true
//                    LaunchedEffect(key1  = email) {
//                        delay(1500)
//                        isLoading = false
//                        isSubmitted = true
//                        onResetSuccess()
//                    }
//                },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = !isLoading
//            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .size(20.dp)
//                            .padding(end = 8.dp),
//                        strokeWidth = 2.dp
//                    )
//                }
//                Text("Send Reset Link")
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ForgotPasswordScreenPreview() {
//    ForgotPasswordScreen()
//}
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    onResetSuccess: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            isLoading = true
            coroutineScope.launch {
                delay(1500) // simulate network delay
                isLoading = false
                isSubmitted = true
                onResetSuccess()
            }
        }) {
            Text(if (isLoading) "Loading..." else "Reset Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isSubmitted) {
            Text("Reset instructions sent to $email")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(navController = NavController(LocalContext.current))
}
