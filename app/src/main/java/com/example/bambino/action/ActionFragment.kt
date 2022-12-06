package com.example.bambino.action

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bambino.R
import com.example.bambino.database.ActionsDatabase
import com.example.bambino.databinding.FragmentActionBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActionFragment : Fragment() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var time = System.currentTimeMillis()
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
//            val time = binding.timeTextInput.editText?.text.toString().toLong()

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
                actionViewModel.onAddAction(time, type)
            }
        }

        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select activity time")
                .build()

        binding.timeTextInput.setEndIconOnClickListener {
            picker.show(parentFragmentManager, "tag")
        }

        picker.addOnPositiveButtonClickListener {
//            binding.timeTextInput.
////            setText("${picker.hour}:${picker.minute}")
            Log.i("picker", "${picker.hour}:${picker.minute}")
            time = (picker.hour * 3600000) + (picker.minute * 60000).toLong()
        }


//        binding.timeTextInput.setOnClickListener {
//            pickTime()
//        }


        actionViewModel.navigateToTrackList.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController().navigate(R.id.action_actionFragment_to_trackFragment)
                actionViewModel.doneNavigating()
            }
        }

        return binding.root
    }

}

