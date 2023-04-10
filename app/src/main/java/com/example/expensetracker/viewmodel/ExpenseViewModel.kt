package com.example.expensetracker.viewmodel

import androidx.lifecycle.*
import com.example.expensetracker.model.Expense
import com.example.expensetracker.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val expenses: LiveData<List<Expense>> = repository.getAllExpenses().asLiveData()

    private val _selectedExpense = MutableLiveData<Expense?>(null)
    val selectedExpense: LiveData<Expense?> = _selectedExpense

    fun setSelectedExpense(expense: Expense?) {
        _selectedExpense.value = expense
    }

    fun insert(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }

    fun update(expense: Expense) {
        viewModelScope.launch {
            repository.update(expense)
        }
    }

    fun delete(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }

    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        return repository.getExpensesByCategory(category).asLiveData()
    }
}


