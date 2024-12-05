package com.snackcheck.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.FragmentHistoryBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.adapter.HistoryAdapter
import com.snackcheck.view.home.HomeViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<HomeViewModel> { factory }
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(requireContext(), pref)

        historyAdapter = HistoryAdapter()
        binding.rvHistory.adapter = historyAdapter

        loadHistory()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun loadHistory() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvHistory.visibility = View.GONE
            btnReloadHistory.visibility = View.GONE

            viewModel.getHistory()
            viewModel.historyList.observe(viewLifecycleOwner) { result ->
                progressBar.visibility = View.GONE
                result.onSuccess { historyList ->
                    btnReloadHistory.visibility = View.GONE
                    rvHistory.visibility = View.VISIBLE
                    historyAdapter.submitList(historyList)
                }
                result.onFailure {
                    Toast.makeText(requireContext(), "Failed to load history", Toast.LENGTH_SHORT).show()
                    btnReloadHistory.visibility = View.VISIBLE
                    rvHistory.visibility = View.GONE
                }
            }
        }
    }

}