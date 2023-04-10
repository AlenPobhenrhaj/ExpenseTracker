package com.example.expensetracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentAddEditExpenseBinding
import com.example.expensetracker.model.Expense
import com.example.expensetracker.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEditExpenseFragment : Fragment() {
    private lateinit var binding: FragmentAddEditExpenseBinding
    private val viewModel: ExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        binding.btnSave.setOnClickListener {
            saveOrUpdateExpense()
        }
    }

    private fun setupUI() {
        val currentExpense = viewModel.selectedExpense.value
        if (currentExpense != null) {
            binding.apply {
                etDescription.setText(currentExpense.description)
                etAmount.setText(currentExpense.amount.toString())
                selectCategory(currentExpense.category)
                setTransactionDate(currentExpense.transactionDate)
            }
        }
    }

    private fun saveOrUpdateExpense() {
        val description = binding.etDescription.text.toString().trim()
        val amount = binding.etAmount.text.toString().toDoubleOrNull()
        val category = getSelectedCategory()
        val transactionDate = getDateFromDatePicker()

        if (validateInput(description, amount, category)) {
            // Format transaction date as a string
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val transactionDateString = dateFormat.format(transactionDate)

            // Update the newExpense object based on whether it's a new expense or an update
            val newExpense = viewModel.selectedExpense.value?.copy(
                description = description,
                amount = amount!!,
                category = category,
                transactionDate = transactionDateString
            ) ?: amount?.let {
                Expense(
                    id = 0,
                    description = description,
                    amount = it,
                    category = category,
                    transactionDate = transactionDateString,
                    isReimbursable = false
                )
            }

            if (newExpense != null) {
                viewModel.insert(newExpense)
            }
            viewModel.setSelectedExpense(null) // Clear the selected expense after inserting
            findNavController().navigateUp()
        }
    }

    private fun validateInput(description: String, amount: Double?, category: String): Boolean {
        return when {
            description.isBlank() -> {
                binding.etDescription.error = "Description cannot be empty"
                false
            }
            amount == null || amount <= 0 -> {
                binding.etAmount.error = "Enter a valid amount"
                false
            }
          /*  category.isBlank() -> {
                binding.rgCategory.error = "Please select a category"
                false
            }*/
            else -> true
        }
    }

    private fun getSelectedCategory(): String {
        return when (binding.rgCategory.checkedRadioButtonId) {
            binding.rbFood.id -> "Food"
            binding.rbTransport.id -> "Transport"
            binding.rbOther.id -> "Other"
            else -> "Null"
        }
    }

    private fun FragmentAddEditExpenseBinding.selectCategory(category: String) {
        rgCategory.clearCheck()
        when (category) {
            "Food" -> rbFood.isChecked = true
            "Transport" -> rbTransport.isChecked = true
            "Other" -> rbOther.isChecked = true
            else -> rbOther.isChecked = true
        }
    }

    private fun getDateFromDatePicker(): Date {
        val datePicker = binding.dpTransactionDate
        val year = datePicker.year
        val month = datePicker.month
        val day = datePicker.dayOfMonth
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    private fun FragmentAddEditExpenseBinding.setTransactionDate(dateString: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        if (date != null) {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            dpTransactionDate.updateDate(year, month, day)
        }
    }
}


