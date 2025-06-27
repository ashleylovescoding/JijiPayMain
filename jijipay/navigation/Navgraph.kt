//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.jijipay.Interface.LoginScreen
//import com.example.jijipay.Interface.AuthScreen
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                Surface {
//                    AppNavigation()
//                }
//            }
//        }
//    }
//}
//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "login") {
//        composable("register") {
//            AuthScreen(
//                navController = navController,
//                onAuthSuccess = TODO()
//            )
//        }
//        composable(
//            route = "login/{email}/{role}",
//        ) { backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email") ?: ""
//            val role = backStackEntry.arguments?.getString("role") ?: "tenant"
//            LoginScreen(navController = navController, email = email, role = role)
//        }
//        composable("biller_dashboard") {
//            Text("Biller Dashboard")
//        }
//        composable("tenant_dashboard") {
//
//            Text("Tenant Dashboard")
//        }
//    }
//}
//
//
//@Composable
//fun TenantDashboardScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Tenant Dashboard",
//            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
//        )
//    }
//}
//
//@Composable
//fun BillerDashboardScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Biller Dashboard",
//            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
//        )
//    }
//}