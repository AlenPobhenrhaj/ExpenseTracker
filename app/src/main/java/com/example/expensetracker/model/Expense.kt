package com.example.expensetracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val description: String,
    val amount: Double,
    val category: String,
    val transactionDate: String,
    var isReimbursable: Boolean
) : Parcelable
