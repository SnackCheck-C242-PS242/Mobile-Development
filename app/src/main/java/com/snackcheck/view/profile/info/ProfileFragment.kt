package com.snackcheck.view.profile.info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.snackcheck.R
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.databinding.FragmentProfileBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.home.HomeViewModel
import com.snackcheck.data.pref.dataStore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<ProfileViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(requireContext(), pref)

        viewModel.getProfile()
        viewModel.userData.observe(viewLifecycleOwner) { profileData ->
            if (profileData != null) {
                binding.tvUsernameProfile.text = profileData.username
                binding.tvEmailProfile.text = profileData.email
                binding.tvFullNameProfile.text = profileData.fullName

                if (profileData.profilePhotoUrl == "") {
                    binding.btnUploadPhoto.visibility = View.VISIBLE
                    binding.btnEditPhoto.visibility = View.GONE
                    binding.ivProfilePicture.setImageResource(R.drawable.ic_account)
                } else {
                    Glide.with(requireContext())
                        .load(viewModel.userData.value?.profilePhotoUrl?.toUri())
                        .into(binding.ivProfilePicture)
                    binding.btnUploadPhoto.visibility = View.GONE
                    binding.btnEditPhoto.visibility = View.VISIBLE
                }
            } else {
                binding.tvUsernameProfile.text = (R.string.you).toString()
                binding.tvEmailProfile.text = (R.string.you).toString()
                binding.tvFullNameProfile.text = (R.string.you).toString()

                binding.btnUploadPhoto.visibility = View.VISIBLE
                binding.btnEditPhoto.visibility = View.GONE
                binding.ivProfilePicture.setImageResource(R.drawable.ic_account)
            }
        }

        setFragmentResultListener("profileUpdated") { _, bundle ->
            val isProfileUpdated = bundle.getBoolean("isProfileUpdated", false)
            Log.d("ProfileFragment", "isProfileUpdated: $isProfileUpdated")
            if (isProfileUpdated) {
                viewModel.getProfile()
            }
        }

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnUploadPhoto.setOnClickListener {
                findNavController().navigate(R.id.navigation_photo_profile)
            }
            btnEditPhoto.setOnClickListener {
                findNavController().navigate(R.id.navigation_photo_profile)
            }
        }
    }
}