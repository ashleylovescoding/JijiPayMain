package com.example.jijipay.Interface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jijipay.data.Bill
import com.example.jijipay.viewmodel.BillViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.lazy.items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDashboard(viewModel: BillViewModel = viewModel()) {
    var showAddBill by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All Bills", "Paid", "Pending", "Overdue")
    var bills by remember { mutableStateOf(listOf<Bill>()) }
    var phone by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
//        viewModel.getCurrentUser()?.email?.let { email ->
//            viewModel.getBillsByTenant(email).collectLatest { bills = it }
//        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tenant Dashboard") },
                actions = {
                    TextButton(onClick = { viewModel.logout() }) {
                        Text("Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddBill = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val filteredBills = when (selectedTab) {
                    1 -> bills.filter { it.paid }
                    2 -> bills.filter { !it.paid && viewModel.isDueSoon(it.dueDate) }
                    3 -> bills.filter { !it.paid && viewModel.isOverdue(it.dueDate) }
                    else -> bills
                }
                items(filteredBills) { bill ->
                    TenantBillCard(bill, onPay = { viewModel.markBillAsPaid(bill.id) })
                }
            }
        }
    }

    if (showAddBill) {
        AddBillDialog(
            onDismiss = { showAddBill = false },
            //here add phone to process
            onAdd = {type, amount, dueDate ->
//                viewModel.getCurrentUser()?.email?.let { email ->
                    viewModel.addBill(Bill(0, phone.toString(), type, amount, dueDate, false))
//                }
                showAddBill = false
            }
        )
    }
}

@Composable
fun TenantBillCard(bill: Bill, onPay: () -> Unit) {

}

@Composable
fun BillerBillCard(bill: Bill, onPay: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = bill.type, style = MaterialTheme.typography.titleMedium)
                Text(text = "Amount: $${bill.amount}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Due: ${bill.dueDate}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = if (bill.paid) "Paid" else "Unpaid",
                    color = if (bill.paid) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                )
            }
            if (!bill.paid) {
                Button(onClick = onPay) {
                    Text("Pay")
                }
            }
        }
    }
}

@Composable
fun AddBillDialog(onDismiss: () -> Unit, onAdd: (String, Double, String) -> Unit) {
    var phone by remember { mutableStateOf("")}
    var type by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Bill") },
        text = {
            Column {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { type=it},
                    label={ Text ("Phone number")}
                )
//                add a phone number input
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Bill Type") }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date (YYYY-MM-DD)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    //add phone number check
                    if (  phone.isNotEmpty() && type.isNotEmpty() && amount.toDoubleOrNull() != null && dueDate.isNotEmpty()) {
                        onAdd(type, amount.toDouble(), dueDate)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}