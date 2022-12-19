package com.example.bambino.track

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bambino.R
import com.example.bambino.database.ActionsDatabase
import com.example.bambino.databinding.FragmentTrackBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TrackFragment : Fragment() {

    //    added
    lateinit var trackViewModel: TrackViewModel
    lateinit var adapter: TrackedActionAdapter

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
        val binding = FragmentTrackBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ActionsDatabase.getInstance(application).actionsDatabaseDao
        val viewModelFactory = TrackViewModelFactory(dataSource, application)

        trackViewModel =
            ViewModelProvider(this, viewModelFactory)[TrackViewModel::class.java]


        binding.trackViewModel = trackViewModel

        binding.lifecycleOwner = this

        adapter = TrackedActionAdapter(TrackedActionAdapter.TrackedActionListener { actionId ->
            trackViewModel.onShowDetails(actionId)
        })
        binding.actionsList.adapter = adapter


        trackViewModel.currentDayActions.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }


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
            binding.chosenDateTextView.text = trackViewModel.currentDayStr
            Log.i(
                "TrackFragment", "${trackViewModel.currentDay} - ${trackViewModel.dayEnd}"
            )
        }


        trackViewModel.navigateToActionCreation.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController()
                    .navigate(R.id.action_trackFragment_to_actionFragment)
                trackViewModel.doneNavigating()
            }
        }


        //DETAILS
        trackViewModel.details.observe(viewLifecycleOwner) {
            if (it > 0) {
                uiScope.launch {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(
                            "${trackViewModel.getActionDetails(it).actionType} - ${
                                SimpleDateFormat("HH:mm", Locale.UK)
                                    .format(trackViewModel.getActionDetails(it).actionTime)
                            }"
                        )
                        .setMessage(
                            "Baby's humour: ${trackViewModel.getActionDetails(it).actionHumour} \n\nDescription: ${
                                trackViewModel.getActionDetails(
                                    it
                                ).actionDescription
                            }"
                        ).setNeutralButton("Remove") { _, _ ->
                            uiScope.launch {
                                trackViewModel.deleteAction(it)
                            }
                        }
                        .setPositiveButton("Close") { _, _ ->

                        }
                        .show()
                }
            }
        }



        return binding.root
    }

    private fun filterDatabase(dayStart: Long, dayEnd: Long) {
        trackViewModel.filterDatabase(dayStart, dayEnd).observe(viewLifecycleOwner) { list ->
            list.let {
                adapter.submitList(it)
            }
        }
    }

}