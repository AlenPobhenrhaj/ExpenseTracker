package com.example.expensetracker.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.databinding.ItemExpenseBinding
import com.example.expensetracker.model.Expense
import java.util.*

class ExpenseListAdapter(
   private val onExpenseClick: (Expense) -> Unit,
   private val onIsReimbursableChanged: (Expense) -> Unit,
   private val onDeleteExpense: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseListAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense, onExpenseClick, onIsReimbursableChanged, onDeleteExpense)
    }

    class ExpenseViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense, onExpenseClick: (Expense) -> Unit,onIsReimbursableChanged: (Expense) -> Unit, onDeleteExpense: (Expense) -> Unit ) {
            binding.apply {
                cbReimbursable.apply {
                    isChecked = expense.isReimbursable
                    setOnCheckedChangeListener { _, isChecked ->
                        expense.isReimbursable = isChecked
                        // Update the expense in the database with the new isReimbursable value
                        onIsReimbursableChanged(expense)
                    }
                }
                tvDescription.text = expense.description
                tvAmount.text = String.format("RM%.2f", expense.amount)

                // Format transaction date as a string
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                tvDate.text = expense.transactionDate

                // Set category icon for ImageView
                // You can implement a function to select the appropriate drawable for each category.
                ivCategory.setImageResource(R.drawable.ic_food)

                itemView.setOnClickListener {
                    onExpenseClick(expense)
                }

                btnDeleteExpense.setOnClickListener {
                    onDeleteExpense(expense)
                }
            }
        }

    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}
