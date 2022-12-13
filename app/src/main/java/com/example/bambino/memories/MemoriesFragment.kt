package com.example.bambino.memories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.bambino.R
import com.example.bambino.database.MemoriesDatabase
import com.example.bambino.databinding.FragmentMemoriesBinding

class MemoriesFragment : Fragment() {

    lateinit var memoriesViewModel: MemoriesViewModel
    lateinit var adapter: MemoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMemoriesBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = MemoriesDatabase.getInstance(application).memoriesDatabaseDao
        val viewModelFactory = MemoriesViewModelFactory(dataSource, application)

        memoriesViewModel = ViewModelProvider(this, viewModelFactory)[MemoriesViewModel::class.java]

        binding.memoriesViewModel = memoriesViewModel


        binding.lifecycleOwner = this


        memoriesViewModel.navigateToMemoryCreation.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController()
                    .navigate(R.id.action_memoriesFragment_to_memoryEntryFragment)
                memoriesViewModel.doneNavigating()
            }
        }

        adapter = MemoryAdapter(this)
        binding.memoriesList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.memoriesList)

        binding.memoriesList.adapter = adapter

        memoriesViewModel.allMemories.observe(viewLifecycleOwner) {
            it?.let {
                adapter.data = it
            }
        }



        return binding.root
    }
}