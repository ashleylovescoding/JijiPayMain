package com.example.jijipay
import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jijipay.Interface.AuthScreen
import com.example.jijipay.Interface.BillerDashboard
import com.example.jijipay.Interface.LoadingScreen
import com.example.jijipay.Interface.LoginScreen
import com.example.jijipay.Interface.SelectionDashboard
import com.example.jijipay.Interface.TenantDashboard
import com.example.jijipay.ui.theme.JijipayTheme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : ComponentActivity() {
    private var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JijipayTheme {
                JijipayApp()
            }
        }
        databaseReference = FirebaseDatabase.getInstance().reference
    }
}



@Composable
fun JijipayApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("auth") {
            AuthScreen(
                navController = navController,
                onAuthSuccess = {
                    navController.navigate("loading") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("selectiondash") {
            SelectionDashboard(navController)
        }


        composable("loading") {
            LoadingScreen(
                navController = navController,
                onLoadingComplete = { role ->
                    when (role) {
                        "tenant" -> navController.navigate("tenant_dashboard")
                        "biller" -> navController.navigate("biller_dashboard")

                    }
                }
            )
        }

        composable("tenant_dashboard") {
            TenantDashboard()
        }

        composable("biller_dashboard") {
            BillerDashboard()
        }
    }
}




