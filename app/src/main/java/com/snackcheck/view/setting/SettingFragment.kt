package com.snackcheck.view.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.snackcheck.R
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.FragmentSettingBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.login.LoginActivity


class SettingFragment : Fragment() {

    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<SettingFragmentViewModel> {
        requireActivity().let { activity ->
            ViewModelFactory.getInstance(activity.applicationContext, pref)
        }
    }
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(requireContext(), pref)

        setupAction()
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}