package com.example.jijipay.repository

import Bill
import android.net.Uri
import com.example.jijipay.data.BillDao
import com.example.jijipay.model.BillItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface BillRepository {
    fun getAllBills(): Flow<List<BillItem>>
    fun fetchBillsFromFirebase(): Flow<List<Bill>>
    suspend fun getBillById(id: Int): BillItem?
    suspend fun insertBill(bill: BillItem)
    suspend fun deleteBill(bill: BillItem)
    suspend fun updateBill(bill: BillItem)
    suspend fun uploadToFirebase(bill: BillItem)
    suspend fun updateBillFirebase(bill: BillItem)
    suspend fun deleteBillFirebase(bill: BillItem)
    suspend fun uploadImageToFirebase(imageUri: Uri?): String
}

class BillRepositoryImpl(private val billDao: BillDao) : BillRepository {

    override fun getAllBills(): Flow<List<BillItem>> {
        return billDao.getAllBills()
    }

    override fun fetchBillsFromFirebase(): Flow<List<BillItem>> = callbackFlow {
        val dbref = FirebaseDatabase.getInstance().reference.child("bills")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bills = mutableListOf<BillItem>()
                for (child in snapshot.children) {
                    val bill = child.getValue(BillItem::class.java)
                    bill?.let { bills.add(it) }
                }
                trySend(bills).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbref.addValueEventListener(listener)
        awaitClose { dbref.removeEventListener(listener) }
    }

    override suspend fun getBillById(id: Int): BillItem? {
        return billDao.getBillById(id)
    }

    override suspend fun insertBill(bill: BillItem) {
        return billDao.insertBill(bill)
    }

    override suspend fun deleteBill(bill: BillItem) {
        return billDao.deleteBill(bill)
    }

    override suspend fun updateBill(bill: BillItem) {
        return billDao.updateBill(bill)
    }

    override suspend fun uploadToFirebase(bill: BillItem) {
        val database = FirebaseDatabase.getInstance().reference
        val newBillRef = database.child("bills").push()
        val firebaseId = newBillRef.key ?: return
        val billWithId = bill.copy(firebase_id = firebaseId)
        newBillRef.setValue(billWithId)
    }

    override suspend fun uploadImageToFirebase(imageUri: Uri?): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("bill_images/${UUID.randomUUID()}.jpg")
        if (imageUri != null) {
            try {
                imageRef.putFile(imageUri).await()
                return imageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                throw Exception("Failed to upload image: ${e.message}", e)
            }
        }
        return imageRef.downloadUrl.await().toString()
    }

    override suspend fun updateBillFirebase(bill: BillItem) {
        val firebaseId = bill.firebase_id
        if (firebaseId.isNotEmpty()) {
            val dbref = FirebaseDatabase.getInstance().reference.child("bills").child(firebaseId)
            dbref.setValue(bill).await()
        } else {
            throw IllegalArgumentException("Firebase id is empty")
        }
    }

    override suspend fun deleteBillFirebase(bill: BillItem) {
        val firebaseId = bill.firebase_id
        if (firebaseId.isNotEmpty()) {
            val dbref = FirebaseDatabase.getInstance().reference.child("bills").child(firebaseId)
            dbref.removeValue().await()
        } else {
            throw IllegalArgumentException("Firebase id is empty")
        }
    }
}
