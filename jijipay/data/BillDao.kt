@file:Suppress("AndroidUnresolvedRoomSqlReference")

package com.example.jijipay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jijipay.data.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Query("SELECT * FROM bills WHERE tenantEmail = :email")
    fun getBillsByTenant(email: String): Flow<List<bill>>

    @Query("SELECT * FROM bills")
    fun getAllBills(): Flow<List<bill>>

    @Insert
    suspend fun insert(bill: bill)

    @Query("UPDATE bills SET isPaid = 1 WHERE id = :billId")
    suspend fun markAsPaid(billId: Int)
}
