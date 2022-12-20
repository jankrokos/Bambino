package com.example.bambino.memoentry

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
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
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class MemoryEntryFragment : Fragment() {

    private lateinit var memoryEntryViewModel: MemoryEntryViewModel
    private var photoStringUri: String = ""
    private var date: Long = System.currentTimeMillis()
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

        binding.memoryDateTextInput.editText?.text = Editable.Factory.getInstance().newEditable(
            SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                .format(date).toString()
        )

        //NAVIGATING BACK TO MEMORIES SCREEN + NEW MEMORY INSERTION
        binding.addMemoryButton.setOnClickListener {
            description = binding.memoryDescriptionTextInput.editText?.text.toString()

            if (photoStringUri == "") {
                Snackbar.make(
                    requireView(),
                    "Select photo to create memory!",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("SELECT") {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                    .show()
            } else if (description == "") {
                Snackbar.make(requireView(), "Description can't be empty!", Snackbar.LENGTH_SHORT)
                    .show()
            } else if (description.length > 40) {
                Snackbar.make(requireView(), "Description can't contain over 40 characters!", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                memoryEntryViewModel.onAddMemory(photoStringUri, date, description)
            }
        }

        memoryEntryViewModel.navigateToMemoriesList.observe(viewLifecycleOwner) {
            if (it) {
//                this.findNavController()
//                    .navigate(R.id.action_memoryEntryFragment_to_memoriesFragment)
                this.findNavController().navigateUp()

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

        binding.memoryDateTextInput.setOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }

        datePicker.addOnPositiveButtonClickListener {
            date = datePicker.selection!!
            binding.memoryDateTextInput.editText?.text = Editable.Factory.getInstance().newEditable(
                SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                    .format(date).toString()
            )
        }


        return binding.root
    }


    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                requireContext().contentResolver.takePersistableUriPermission(uri, flag)
                photoStringUri = uri.toString()
                memoryEntryViewModel.setMemoryPhotoUri(photoStringUri)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


}