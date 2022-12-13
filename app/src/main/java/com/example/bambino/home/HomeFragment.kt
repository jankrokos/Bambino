package com.example.bambino.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bambino.R
import com.example.bambino.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private var familyUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("PhotoPicker", "oncreateviewcalled")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.familyPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        Glide
            .with(this)
            .load("content://com.android.providers.media.documents/document/image%3A43999")
            .centerCrop()
            .error(R.drawable.ic_baseline_bathtub_48)
            .into(binding.familyPhoto)

        homeViewModel.changePhoto.observe(viewLifecycleOwner) {
            if (it) {
                Glide
                    .with(this)
                    .load(homeViewModel.familyPhotoUri.value)
                    .centerCrop()
                    .error(R.drawable.ic_baseline_bathtub_48)
                    .into(binding.familyPhoto)

                homeViewModel.photoChanged()
            }
        }


        return binding.root
    }


    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            familyUri = uri
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: ${uri.toString()}")
                homeViewModel.setFamilyPhotoUri(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


}
