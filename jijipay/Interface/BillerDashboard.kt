package com.example.jijipay.Interface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jijipay.data.Bill
import com.example.jijipay.data.User
import com.example.jijipay.viewmodel.BillViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillerDashboard(viewModel: BillViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All Tenants", "Unpaid Bills")

    var bills by remember { mutableStateOf(emptyList<Bill>()) }
    var tenants by remember { mutableStateOf(emptyList<User>()) }

    LaunchedEffect(Unit) {
        viewModel.getAllBills().collectLatest { bills = it }
        tenants = viewModel.getAllTenants()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biller Dashboard") },
                actions = {
                    TextButton(onClick = { viewModel.logout() }) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                if (selectedTab == 0) {
                    items(tenants) { tenant ->
                        TenantCard(tenant)
                    }
                } else {
                    val unpaidBills = bills.filter { !it.paid }
                    items(unpaidBills) { bill ->
                        BillCard(bill, onPay = { /* Biller doesn't pay */ })
                    }
                }
            }
        }
    }
}

@Composable
fun TenantCard(tenant: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tenant.name, style = MaterialTheme.typography.titleMedium)
            Text(text = tenant.email, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BillCard(bill: Bill, onPay: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Bill ID: ${bill.id}", style = MaterialTheme.typography.titleMedium)
            Text("Amount: KES ${bill.amount}", style = MaterialTheme.typography.bodyMedium)
            Text("Paid: ${bill.paid}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BillerDashboardPreview() {
    val dummyTenants = listOf(
        User(name = "John Smith", email = "john@example.com", role = "tenant"),
        User(name = "Jane Smith", email = "jane@example.com", role = "tenant")
    )
    val dummyBills = listOf(
        Bill(id = 1, tenantEmail = "john@example.com", type = "Water", amount = 1500.0, dueDate = "2025-07-01", paid = false),
        Bill(id = 2, tenantEmail = "jane@example.com", type = "Rent", amount = 2500.0, dueDate = "2025-07-05", paid = true)
    )

    // Replace with your actual dashboard composable
    BillerDashboardScreen(users = dummyTenants, bills = dummyBills)
}

//    MaterialTheme {
//        Column {
//            dummyTenants.forEach { TenantCard(it) }
//            dummyBills.forEach { BillCard(it, onPay = {}) }
//        }
//    }
//}
