package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.database.AppDatabase
import com.example.expensetracker.repository.ExpenseRepository

class MyApplication : Application() {
    val repository: ExpenseRepository by lazy {
        ExpenseRepository(AppDatabase.getInstance(applicationContext).expenseDao())
    }
}
