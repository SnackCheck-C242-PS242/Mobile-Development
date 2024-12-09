package com.snackcheck.view.history.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.FragmentHistoryBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.adapter.HistoryAdapter

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<HistoryViewModel> { factory }
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

        historyAdapter = HistoryAdapter {
            val bundle = Bundle().apply {
                putString("snackId", it.snackId)
            }

            findNavController().navigate(R.id.action_history_to_history_detail, bundle)
        }
        binding.rvHistory.adapter = historyAdapter

        loadHistory()
        setupRecyclerView()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnClearHistory.setOnClickListener {
                viewModel.clearHistory()
                viewModel.responseClearHistory.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is ResultState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            rvHistory.visibility = View.GONE
                            btnReloadHistory.visibility = View.GONE
                            tvNoHistory.visibility = View.GONE
                        }

                        is ResultState.Success -> {
                            progressBar.visibility = View.GONE
                            rvHistory.visibility = View.GONE
                            btnReloadHistory.visibility = View.VISIBLE
                            tvNoHistory.visibility = View.VISIBLE
                        }

                        is ResultState.Error -> {
                            progressBar.visibility = View.GONE
                            rvHistory.visibility = View.GONE
                            btnReloadHistory.visibility = View.VISIBLE
                            tvNoHistory.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
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
            tvNoHistory.visibility = View.GONE

            viewModel.getHistory()
            viewModel.historyList.observe(viewLifecycleOwner) { result ->
                progressBar.visibility = View.GONE
                result.onSuccess { historyList ->
                    if (historyList.isNullOrEmpty()) {
                        tvNoHistory.visibility = View.VISIBLE
                        btnReloadHistory.visibility = View.VISIBLE
                        rvHistory.visibility = View.GONE
                        return@observe
                    } else {
                        tvNoHistory.visibility = View.GONE
                        btnReloadHistory.visibility = View.GONE
                        rvHistory.visibility = View.VISIBLE
                        historyAdapter.submitList(historyList)
                        return@observe
                    }
                }
                result.onFailure {
                    tvNoHistory.visibility = View.VISIBLE
                    btnReloadHistory.visibility = View.VISIBLE
                    rvHistory.visibility = View.GONE
                }
            }
        }
    }

}