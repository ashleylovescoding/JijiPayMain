package com.example.jijipay.data
import Bill
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jijipay.data.dao.BillDao

@Database(entities = [Bill::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao
    abstract fun userDao(): UserDao
    abstract fun Bill(): Bill
}


