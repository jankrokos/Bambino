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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bambino.MainActivity
import com.example.bambino.R
import com.example.bambino.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        Glide
            .with(this)
            .load(R.drawable.welcome_back_image)
            .centerCrop()
            .into(binding.familyPhoto)

        Glide
            .with(this)
            .load(R.drawable.kid_eating)
            .centerCrop()
            .into(binding.kidEatingPhoto)

        Glide
            .with(this)
            .load(R.drawable.camera_photo_mommy)
            .centerCrop()
            .into(binding.makingPhotoPhoto)

        binding.goToActionsAdd.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_actionFragment)
        }

        binding.goToActionsList.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_trackFragment)
        }

        binding.goToMemoAdd.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_memoryEntryFragment)
        }

        binding.goToMemoList.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_memoriesFragment)
        }


        return binding.root
    }
}
