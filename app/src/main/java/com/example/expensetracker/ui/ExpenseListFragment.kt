package com.example.expensetracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.adapter.ExpenseListAdapter
import com.example.expensetracker.databinding.FragmentExpenseListBinding
import com.example.expensetracker.model.Expense
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModelFactory
import com.example.expensetracker.MyApplication

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpenseViewModel by navGraphViewModels(R.id.nav_graph) {
        ExpenseViewModelFactory((requireActivity().application as MyApplication).repository)
    }

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

        setupRecyclerView()
        setupFab()
        observeExpenses()
    }

    private fun setupRecyclerView() {
        expenseListAdapter = ExpenseListAdapter { expense ->
            viewModel.setSelectedExpense(expense)
            val action = ExpenseListFragmentDirections.actionExpenseListFragmentToAddEditExpenseFragment(expense)
            findNavController().navigate(action)
        }
        binding.rvExpenses.apply {
            adapter = expenseListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
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
