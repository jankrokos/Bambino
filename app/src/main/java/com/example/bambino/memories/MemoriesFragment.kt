package com.example.bambino.memories

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.bambino.R
import com.example.bambino.database.MemoriesDatabase
import com.example.bambino.databinding.FragmentMemoriesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MemoriesFragment : Fragment() {

    private lateinit var memoriesViewModel: MemoriesViewModel
    private lateinit var adapter: MemoryAdapter

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


        adapter = MemoryAdapter(
            MemoryAdapter.MemoryListener(
                { memoryId ->
                    memoriesViewModel.onEditMemoryClicked(memoryId)
                },
                { memoryId ->
                    uiScope.launch {
                        val memo = memoriesViewModel.getMemoryWithId(memoryId)
                        val imageUri = Uri.parse(memo.memoryPhotoUri)
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(
                            Intent
                                .EXTRA_TEXT,
                            "${
                                SimpleDateFormat(
                                    "EEEE, d MMM yyyy",
                                    Locale.UK
                                ).format(memo.memoryDate)
                            }\n${memo.memoryDescription}"
                        )
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                        shareIntent.type = "image/jpeg"
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(Intent.createChooser(shareIntent, "Send"))
                    }

                })
        )


        memoriesViewModel.navigateToEditMemory.observe(viewLifecycleOwner) { memory ->
            memory?.let {
                this.findNavController().navigate(
                    MemoriesFragmentDirections.actionMemoriesFragmentToEditMemoryFragment(memory)
                )
                memoriesViewModel.onEditMemoryNavigated()
            }
        }

        binding.memoriesList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.memoriesList)

        binding.memoriesList.adapter = adapter

        memoriesViewModel.allMemories.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        return binding.root
    }
}