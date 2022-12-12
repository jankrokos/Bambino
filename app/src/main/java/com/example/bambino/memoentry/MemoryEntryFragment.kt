package com.example.bambino.memoentry

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
import com.example.bambino.R
import com.example.bambino.database.MemoriesDatabase
import com.example.bambino.databinding.FragmentMemoryEntryBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MemoryEntryFragment : Fragment() {

    private lateinit var memoryEntryViewModel: MemoryEntryViewModel
    private var photoStringUri: String = ""
    private var date: Long = 0
    private var description: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMemoryEntryBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = MemoriesDatabase.getInstance(application).memoriesDatabaseDao
        val viewModelFactory = MemoryEntryViewModelFactory(dataSource)
        memoryEntryViewModel =
            ViewModelProvider(this, viewModelFactory)[MemoryEntryViewModel::class.java]
        binding.memoryEntryViewModel = memoryEntryViewModel


        //NAVIGATING BACK TO MEMORIES SCREEN + NEW MEMORY INSERTION
        binding.addMemoryButton.setOnClickListener {
            description = binding.memoryDescriptionTextInput.editText?.text.toString()
            memoryEntryViewModel.onAddMemory(photoStringUri, date, description)
        }

        memoryEntryViewModel.navigateToMemoriesList.observe(viewLifecycleOwner) {
            if (it) {
//                Log.i("MemoEntry", "$photoStringUri, $date, $description ")
                this.findNavController()
                    .navigate(R.id.action_memoryEntryFragment_to_memoriesFragment)
                memoryEntryViewModel.doneNavigating()
            }
        }

        //CHANGING MEMORY PHOTO ON IMAGE CLICK
        binding.newMemoryPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        memoryEntryViewModel.changePhoto.observe(viewLifecycleOwner) {
            if (it) {

                Glide
                    .with(this)
                    .load(memoryEntryViewModel.memoryPhotoStringUri.value)
                    .centerCrop()
                    .into(binding.newMemoryPhoto)

                memoryEntryViewModel.photoChanged()
            }
        }

        //DATE INPUT
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        binding.memoryDateTextInput.setEndIconOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }

        datePicker.addOnPositiveButtonClickListener {
            date = datePicker.selection!!
        }


        return binding.root
    }


    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                photoStringUri = uri.toString()
                memoryEntryViewModel.setMemoryPhotoUri(photoStringUri)
                Log.d("PhotoPicker", "Selected URI: ${uri.toString()}")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


}