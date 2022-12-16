package com.example.bambino.editmemory

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bambino.R
import com.example.bambino.database.MemoriesDatabase
import com.example.bambino.database.Memory
import com.example.bambino.databinding.FragmentEditMemoryBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditMemoryFragment : Fragment() {

    private lateinit var editMemoryViewModel: EditMemoryViewModel
    private var memoUri = ""
    private var memoDate: Long = 0
    private var memoDescription = ""
    var changed: Boolean = false

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditMemoryBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = MemoriesDatabase.getInstance(application).memoriesDatabaseDao
        val arguments = EditMemoryFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = EditMemoryViewModelFactory(dataSource, arguments.memoryKey)
        editMemoryViewModel =
            ViewModelProvider(this, viewModelFactory)[EditMemoryViewModel::class.java]
        binding.editMemoryViewModel = editMemoryViewModel
        binding.lifecycleOwner = this


        uiScope.launch {
            val memo = editMemoryViewModel.getCurrentMemory()

            memoUri = memo.memoryPhotoUri
            memoDate = memo.memoryDate
            memoDescription = memo.memoryDescription

            Glide
                .with(requireContext())
                .load(memoUri)
                .centerCrop()
                .into(binding.newMemoryPhoto)

            binding.memoryDateTextInput.editText?.text = Editable.Factory.getInstance().newEditable(
                SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                    .format(memoDate).toString()
            )

            binding.memoryDescriptionTextInput.editText?.text =
                Editable.Factory.getInstance().newEditable(
                    memoDescription
                )
        }

        editMemoryViewModel.navigateToMemoriesListDel.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController()
                    .navigate(R.id.action_editMemoryFragment_to_memoriesFragment)
                editMemoryViewModel.doneNavigatingDel()
                Snackbar.make(
                    requireView(),
                    "Memory deleted",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.confirmEditMemoryButton.setOnClickListener {
            memoDescription = binding.memoryDescriptionTextInput.editText?.text.toString()
            if (memoUri == "") {
                Snackbar.make(
                    requireView(),
                    "Select photo to create memory!",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("SELECT") {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                    .show()
            } else if (memoDescription == "") {
                Snackbar.make(requireView(), "Description can't be empty!", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                editMemoryViewModel.onUpdate(memoUri, memoDate, memoDescription)
            }
        }


        editMemoryViewModel.navigateToMemoriesListUp.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController()
                    .navigate(R.id.action_editMemoryFragment_to_memoriesFragment)
                editMemoryViewModel.doneNavigatingUp()
                Snackbar.make(
                    requireView(),
                    "Memory updated",
                    Snackbar.LENGTH_SHORT
                ).show()
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
            memoDate = datePicker.selection!!
            changed = true
            binding.memoryDateTextInput.editText?.text = Editable.Factory.getInstance().newEditable(
                SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                    .format(memoDate).toString()
            )
        }

        //CHANGING MEMORY PHOTO ON IMAGE CLICK
        binding.newMemoryPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        editMemoryViewModel.changePhoto.observe(viewLifecycleOwner) {
            if (it) {
                Glide
                    .with(this)
                    .load(editMemoryViewModel.memoryPhotoStringUri.value)
                    .centerCrop()
                    .into(binding.newMemoryPhoto)

                editMemoryViewModel.photoChanged()
            }
        }

        return binding.root
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                changed = true
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                requireContext().contentResolver.takePersistableUriPermission(uri, flag)
                memoUri = uri.toString()
                editMemoryViewModel.setMemoryPhotoUri(memoUri)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

}