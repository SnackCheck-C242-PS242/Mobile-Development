package com.snackcheck.view.profile.photo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snackcheck.helper.imageCompressor
import com.snackcheck.helper.uriToFile
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.FragmentPhotoProfileBinding
import com.snackcheck.view.ViewModelFactory
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class PhotoProfileFragment : Fragment() {

    private var _binding: FragmentPhotoProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: UserPreference
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<PhotoProfileViewModel> { factory }
    private var currentPhotoProfileUri: Uri? = null
    private lateinit var previousPhotoProfileUrl : String
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPhotoProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(requireContext(), pref)

        bottomNavigationView = activity?.findViewById(R.id.nav_view)!!
        bottomNavigationView.visibility = View.GONE

        val builder: AlertDialog.Builder =
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
        builder.setView(R.layout.layout_loading)
        val dialog: AlertDialog = builder.create()

        viewModel.getProfile()
        previousPhotoProfileUrl = viewModel.userData.value?.profilePhotoUrl.toString()

        viewModel.userData.observe(viewLifecycleOwner) { profileData ->
            if (profileData != null) {
                if (profileData.profilePhotoUrl == "") {
                    binding.ivProfilePicture.setImageResource(R.drawable.ic_account)
                } else {
                    Glide.with(requireContext())
                        .load(viewModel.userData.value?.profilePhotoUrl?.toUri())
                        .into(binding.ivProfilePicture)
                }
            } else {
                binding.ivProfilePicture.setImageResource(R.drawable.ic_account)
            }
        }

        viewModel.postResponseResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Success -> {
                    dialog.dismiss()

                    val resultBundle = Bundle().apply {
                        putBoolean("isProfileUpdated", true)
                    }
                    setFragmentResult("profileUpdated", resultBundle)

                    findNavController().navigate(R.id.navigation_profile)
                    bottomNavigationView.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                else -> dialog.dismiss()
            }
        }

        viewModel.putResponseResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Success -> {
                    dialog.dismiss()

                    val resultBundle = Bundle().apply {
                        putBoolean("isProfileUpdated", true)
                    }
                    parentFragmentManager.setFragmentResult("profileUpdated", resultBundle)

                    findNavController().navigate(R.id.navigation_profile)
                    bottomNavigationView.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                else -> dialog.dismiss()
            }
        }


        viewModel.currentPhotoProfileUri?.let {
            currentPhotoProfileUri = it
            showImage()
        }

        setupAction()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
            showImage()
        }
    }

    private fun showImage() {
        if (currentPhotoProfileUri != null) {
            binding.ivProfilePicture.setImageURI(currentPhotoProfileUri)
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(requireContext().cacheDir.resolve("cropped_image.jpg"))

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(500, 500)
            .start(requireContext(), this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                currentPhotoProfileUri = resultUri
                viewModel.currentPhotoProfileUri = currentPhotoProfileUri
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.printStackTrace()
            Log.e("UCrop", "Crop error: $cropError")
        }
    }

    private fun postProfilePhoto() {
        currentPhotoProfileUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).imageCompressor()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val multipartBody = MultipartBody.Part.createFormData(
                "profilePhoto",
                imageFile.name,
                requestImageFile
            )

            viewModel.postPhotoProfile(multipartBody)
        } ?: showToast(getString(R.string.no_image_selected))
    }

    private fun putProfilePhoto() {
        currentPhotoProfileUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).imageCompressor()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val multipartBody = MultipartBody.Part.createFormData(
                "profilePhoto",
                imageFile.name,
                requestImageFile
            )

            viewModel.putPhotoProfile(multipartBody)
        } ?: showToast(getString(R.string.no_image_selected))
    }

    private fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    private fun setupAction() {
        binding.apply {
            btnCancel.setOnClickListener {
                findNavController().navigate(R.id.navigation_profile)
                bottomNavigationView.visibility = View.VISIBLE
            }

            btnChooseFromGallery.setOnClickListener {
                startGallery()
            }
        }

        if (currentPhotoProfileUri == null){
            binding.btnSaveChanges.setOnClickListener{
                postProfilePhoto()
            }
        } else {
            binding.btnSaveChanges.setOnClickListener{
                putProfilePhoto()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }
}