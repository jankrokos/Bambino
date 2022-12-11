package com.example.bambino.action

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bambino.MainActivity
import com.example.bambino.R
import com.example.bambino.database.ActionsDatabase
import com.example.bambino.databinding.FragmentActionBinding
import com.example.bambino.track.TrackViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ActionFragment : Fragment() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var time: Long = 0
    private var date: Long = 0
    private var actionWhen: Long = 0
    private var type = ""


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentActionBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ActionsDatabase.getInstance(application).actionsDatabaseDao


        val viewModelFactory = ActionViewModelFactory(dataSource)

        val actionViewModel =
            ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]

        binding.actionViewModel = actionViewModel



        binding.addActivityButton.setOnClickListener {
            actionWhen = date + time

            //TYPE INPUT
            when (binding.toggleButton.checkedButtonId) {
                R.id.bath_button ->
                    type = "Bath"
                R.id.eat_button ->
                    type = "Eat"
                R.id.diaper_button ->
                    type = "Diaper"
                R.id.sleep_button ->
                    type = "Sleep"
            }
            Log.i("ActionFragment", "$time, $type")
            uiScope.launch(Dispatchers.IO) {
                actionViewModel.onAddAction(actionWhen, type)
            }
        }


        //TIME INPUT
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select activity time")
                .build()

        binding.timeTextInput.setEndIconOnClickListener {
            timePicker.show(parentFragmentManager, "tag")
        }

        timePicker.addOnPositiveButtonClickListener {
            Log.i("picker", "${timePicker.hour}:${timePicker.minute}")
            actionViewModel.timeString = "${timePicker.hour}:${timePicker.minute}"
            time = (((timePicker.hour - 1) * 3600000) + (timePicker.minute * 60000)).toLong()
        }


        //DATE INPUT
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        binding.dateTextInput.setEndIconOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }

        datePicker.addOnPositiveButtonClickListener {
            actionViewModel.dateString =
                SimpleDateFormat("yyyy.MM.dd", Locale.UK)
                    .format(datePicker.selection).toString()
            date = datePicker.selection!!
        }


        //NAVIGATING BACK TO THE LIST
        actionViewModel.navigateToTrackList.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController().navigate(R.id.action_actionFragment_to_trackFragment)
                actionViewModel.doneNavigating()
            }
        }

        return binding.root
    }

}

