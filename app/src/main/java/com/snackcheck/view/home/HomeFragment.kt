package com.snackcheck.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.FragmentHomeBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.adapter.HistoryAdapter
import com.snackcheck.view.adapter.NewsAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<HomeViewModel> { factory }
    private lateinit var historyAdapter: HistoryAdapter
    private var newsAdapter: NewsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

            findNavController().navigate(R.id.action_home_to_history_detail, bundle)
        }

        binding.rvHistory.adapter = historyAdapter
        binding.rvNews.adapter = newsAdapter

        viewModel.getNews()
        viewModel.newsResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBarNews.visibility = View.VISIBLE
                    binding.rvNews.visibility = View.GONE
                }

                is ResultState.Success -> {
                    binding.progressBarNews.visibility = View.GONE
                    binding.rvNews.visibility = View.VISIBLE
                    newsAdapter = NewsAdapter(result.data)
                    binding.rvNews.apply {
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = newsAdapter
                    }
                }

                is ResultState.Error -> {
                    binding.progressBarNews.visibility = View.GONE
                    binding.rvNews.visibility = View.GONE
                    binding.tvNoNews.visibility = View.VISIBLE
                }
            }

        }

        viewModel.getProfile()
        viewModel.userData.observe(viewLifecycleOwner) { profileData ->
            if (profileData != null) {
                binding.tvUserFullname.text = profileData.fullName
                if (profileData.profilePhotoUrl == "") {
                    binding.ivProfile.setImageResource(R.drawable.ic_account)
                    viewModel.getProfile()
                } else {
                    Glide.with(requireContext())
                        .load(viewModel.userData.value?.profilePhotoUrl?.toUri())
                        .into(binding.ivProfile)
                }
            } else {
                binding.tvUserFullname.text = (R.string.you).toString()
            }
        }

        setupAction()
        loadHistory()
        setupRecyclerView()
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

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun setupAction() {
        binding.apply {
            actionPrediction.setOnClickListener {
                findNavController().navigate(R.id.navigation_prediction)
            }

            actionHistory.setOnClickListener {
                findNavController().navigate(R.id.navigation_history)
            }

            btnReloadHistory.setOnClickListener {
                loadHistory()
            }
        }
    }
}