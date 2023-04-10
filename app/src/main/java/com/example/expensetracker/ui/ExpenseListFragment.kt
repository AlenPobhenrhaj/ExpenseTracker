package com.example.expensetracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.adapter.ExpenseListAdapter
import com.example.expensetracker.databinding.FragmentExpenseListBinding
import com.example.expensetracker.model.Expense
import com.example.expensetracker.viewmodel.ExpenseViewModel

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var expenseListAdapter: ExpenseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ExpenseViewModel::class.java)

        setupRecyclerView()
        setupFab()
        observeExpenses()
    }

    private fun setupRecyclerView() {
        binding.rvExpenses.apply {
            adapter = expenseListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        // Update the onExpenseClick lambda to set the selected expense
        expenseListAdapter.onExpenseClick = { expense ->
            viewModel.setSelectedExpense(expense)
            val action = ExpenseListFragmentDirections.actionExpenseListFragmentToAddEditExpenseFragment(expense)
            findNavController().navigate(action)
        }
    }


    private fun setupFab() {
        binding.fabAddExpense.setOnClickListener {
            navigateToAddEditExpenseFragment()
        }
    }

    private fun observeExpenses() {
        viewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            expenseListAdapter.submitList(expenses)
        }
    }

    private fun navigateToAddEditExpenseFragment(expense: Expense? = null) {
        val bundle = Bundle().apply {
            putParcelable("expense", expense)
        }
        findNavController().navigate(R.id.action_expenseListFragment_to_addEditExpenseFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
