package com.example.jijipay.Interface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jijipay.data.Bill
import com.example.jijipay.data.User

@Composable
fun BillerDashboardScreen(users: List<User>, bills: List<Bill>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Biller Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users) { user ->
                val userBills = bills.filter { it.tenantEmail == user.email }

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                userBills.forEach { bill ->
                    BillCard(bill)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun BillCard(bill: Bill) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (bill.paid) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Type: ${bill.type}")
            Text(text = "Amount: KES ${bill.amount}")
            Text(text = "Due: ${bill.dueDate}")
            Text(text = "Status: ${if (bill.paid) "Paid" else "Unpaid"}")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BillerDashboardScreenPreview() {
    val dummyUsers = listOf(
        User(name = "John Doe", email = "john@example.com", role = "tenant"),
        User(name = "Jane Doe", email = "jane@example.com", role = "tenant")
    )

    val dummyBills = listOf(
        Bill(id = 1, tenantEmail = "john@example.com", type = "Water", amount = 1200.0, dueDate = "2025-07-01", paid = false),
        Bill(id = 2, tenantEmail = "john@example.com", type = "Electricity", amount = 800.0, dueDate = "2025-07-05", paid = true),
        Bill(id = 3, tenantEmail = "jane@example.com", type = "Security", amount = 600.0, dueDate = "2025-07-03", paid = false)
    )

    BillerDashboardScreen(users = dummyUsers, bills = dummyBills)
}

