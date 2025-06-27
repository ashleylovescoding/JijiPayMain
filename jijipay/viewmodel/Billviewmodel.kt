package com.example.jijipay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.jijipay.data.AppDatabase
import com.example.jijipay.data.Bill
import com.example.jijipay.data.User
import com.example.jijipay.data.BillDao
import com.example.jijipay.data.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class BillViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "estate-sync-db"
    ).fallbackToDestructiveMigration().build()

    private val BillDao: BillDao = db.billDao()
    private val userDao: UserDao = db.userDao()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        viewModelScope.launch {
            if (userDao.getUserByEmail("tenant1@example.com") == null) {
                userDao.insert(User("tenant1@example.com", "tenant", "Tenant One"))
                userDao.insert(User("tenant2@example.com", "tenant", "Tenant Two"))
                userDao.insert(User("biller@example.com", "biller", "Biller One"))
                BillDao.insert(Bill(0, "tenant1@example.com", "Water Levy", 50.0, "2025-06-20", false))
                BillDao.insert(Bill(0, "tenant1@example.com", "Security Fee", 30.0, "2025-06-10", true))
                BillDao.insert(Bill(0, "tenant2@example.com", "Garbage Collection", 20.0, "2025-06-25", false))
            }
        }
    }

    fun getCurrentUser(): User? {
        val email = auth.currentUser?.email ?: return null
        return runBlocking { userDao.getUserByEmail(email) }
    }
    fun getBillsByTenant(email: String): Flow<List<Bill>> = BillDao.getBillsByTenant(email)

    fun getAllBills(): Flow<List<Bill>> = BillDao.getAllBills()

    fun getAllTenants(): List<User> = runBlocking { userDao.getAllTenants() }

    fun addBill(bill: Bill) {
        viewModelScope.launch {
            // Add to Firebase Realtime Database
            val databaseRef = FirebaseDatabase.getInstance().reference
            val newBillRef = databaseRef.child("bills").push()
            val firebaseId = newBillRef.key ?: return@launch

            val billWithId = bill.copy(firebase_id = firebaseId)
            newBillRef.setValue(billWithId)

            // Optional: Save locally
            BillDao.insert(billWithId)
        }
    }

    fun markBillAsPaid(billId: Int) {
        viewModelScope.launch {
            BillDao.markAsPaid(billId)
        }
    }

    fun registerUser(
        email: String,
        password: String,
        name: String,
        role: String,
        onResult: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        userDao.insert(User(email, role, name))
                        onResult(true)
                    }
                } else {
                    onResult(false)
                }
            }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun logout() {
        auth.signOut()
    }

    fun isOverdue(dueDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = sdf.parse(dueDate) ?: return false
        return date.before(Date())
    }

    fun isDueSoon(dueDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = sdf.parse(dueDate) ?: return false
        val diff = (date.time - Date().time) / (1000 * 60 * 60 * 24)
        return diff in 1..15
    }
}
