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
import com.google.android.material.snackbar.Snackbar
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


    private val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private val currentMinutes = Calendar.getInstance().get(Calendar.MINUTE)


    private var time: Long = ((currentHour - 1) * 3600000) + (currentMinutes * 60000).toLong()
    private var date: Long =
        System.currentTimeMillis() - ((currentHour - 1) * 3600000) - (currentMinutes * 60000)

    private var actionWhen: Long = 0
    private var type = ""
    private var humour = 3
    private var description = ""


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


        //SETTING INITIAL DATE AND TIME
        binding.dateTextInput.editText?.text = Editable.Factory.getInstance().newEditable(
            SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                .format(System.currentTimeMillis()).toString()
        )

        binding.timeTextInput.editText?.text = Editable.Factory.getInstance().newEditable(
            SimpleDateFormat("HH:mm", Locale.UK)
                .format(System.currentTimeMillis()).toString()
        )


        binding.addActivityButton.setOnClickListener {
            actionWhen = date + time

            description = binding.descriptionTextInput.editText?.text.toString()

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

            if (description.length > 100) {
                Snackbar.make(
                    requireView(),
                    "Description can't contain over 100 characters!",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            } else if (description == "") {
                Snackbar.make(
                    requireView(),
                    "Description can't be empty!",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                uiScope.launch(Dispatchers.IO) {
                    actionViewModel.onAddAction(actionWhen, type, humour, description)
                }
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
            time = (((timePicker.hour - 1) * 3600000) + (timePicker.minute * 60000)).toLong()

            val hoursStr = if (timePicker.hour < 10) {
                "0" + timePicker.hour.toString()
            } else {
                timePicker.hour.toString()
            }

            val minutesStr = if (timePicker.minute < 10) {
                "0" + timePicker.minute.toString()
            } else {
                timePicker.minute.toString()
            }

            binding.timeTextInput.editText?.text = Editable.Factory.getInstance()
                .newEditable("$hoursStr:$minutesStr")
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
            binding.dateTextInput.editText?.text = Editable.Factory.getInstance().newEditable(
                SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                    .format(datePicker.selection).toString()
            )
            date = datePicker.selection!!
        }


        //HUMOUR INPUT
        binding.slider.addOnChangeListener { _, value, _ ->
            humour = value.toInt()
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

