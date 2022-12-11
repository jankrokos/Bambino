package com.example.bambino.track

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bambino.R
import com.example.bambino.database.ActionsDatabase
import com.example.bambino.databinding.FragmentTrackBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class TrackFragment : Fragment() {

    //    added
    lateinit var trackViewModel: TrackViewModel
    lateinit var adapter: TrackedActionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTrackBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ActionsDatabase.getInstance(application).actionsDatabaseDao
        val viewModelFactory = TrackViewModelFactory(dataSource, application)

        trackViewModel =
            ViewModelProvider(this, viewModelFactory)[TrackViewModel::class.java]


        binding.trackViewModel = trackViewModel

        adapter = TrackedActionAdapter()
        binding.actionsList.adapter = adapter


        trackViewModel.currentDayActions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })


        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        binding.changeDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }

        datePicker.addOnPositiveButtonClickListener {
            trackViewModel.setToday(datePicker.selection!!)
            filterDatabase(trackViewModel.currentDay, trackViewModel.dayEnd)
            Log.i(
                "TrackFragment", "${trackViewModel.currentDay} - ${trackViewModel.dayEnd}"
            )
        }


        binding.lifecycleOwner = this

        trackViewModel.navigateToActionCreation.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController()
                    .navigate(R.id.action_trackFragment_to_actionFragment)
                trackViewModel.doneNavigating()
            }
        }


        return binding.root
    }

    private fun filterDatabase(dayStart: Long, dayEnd: Long) {
        trackViewModel.filterDatabase(dayStart, dayEnd).observe(viewLifecycleOwner) { list ->
            list.let {
                adapter.data = it
            }
        }

    }

}