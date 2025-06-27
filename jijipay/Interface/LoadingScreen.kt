package com.example.jijipay.Interface

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jijipay.viewmodel.BillViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    navController: NavController,
    onLoadingComplete: (String?) -> Unit,
    viewModel: BillViewModel = viewModel()
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "JijiPay",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (errorMessage == null) "Loading your dashboard..." else errorMessage!!,
            style = MaterialTheme.typography.bodyLarge,
            color = if (errorMessage == null) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading && errorMessage == null) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            Button(
                onClick = {
                    isLoading = false
                    val user = viewModel.getCurrentUser()
                    onLoadingComplete(user?.role)
                }
            ) {
                Text("Skip")
            }
        }
    }

    LaunchedEffect(Unit) {
        //make this go to login screen
        navController.navigate("login")
//        val user = viewModel.getCurrentUser()
//        if (user == null) {
//            errorMessage = "User data not found. Please log in again."
//            isLoading = false
//            delay(2000) // Show error for 2 seconds
//            onLoadingComplete(null)
//            return@LaunchedEffect
//        }
//        delay(2000) // Simulate loading
//        if (isLoading) {
//            onLoadingComplete(user.role)
//        }
    }
}