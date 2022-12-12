package com.example.bambino.memoentry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bambino.R
import com.example.bambino.database.MemoriesDatabase
import com.example.bambino.databinding.FragmentMemoryEntryBinding

class MemoryEntryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMemoryEntryBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = MemoriesDatabase.getInstance(application).memoriesDatabaseDao

        val viewModelFactory = MemoryEntryViewModelFactory(dataSource)

        val memoryEntryViewModel =
            ViewModelProvider(this, viewModelFactory)[MemoryEntryViewModel::class.java]

        binding.memoryEntryViewModel = memoryEntryViewModel

        binding.addMemoryButton.setOnClickListener {
            memoryEntryViewModel.onAddMemory()
        }

        memoryEntryViewModel.navigateToMemoriesList.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController()
                    .navigate(R.id.action_memoryEntryFragment_to_memoriesFragment)
                memoryEntryViewModel.doneNavigating()
            }
        }


        return binding.root
    }

}