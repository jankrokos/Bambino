package com.example.bambino.track

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bambino.R
import com.example.bambino.database.ActionsDatabase
import com.example.bambino.database.TrackedAction
import com.example.bambino.databinding.FragmentTrackBinding

class TrackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTrackBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ActionsDatabase.getInstance(application).actionsDatabaseDao
        val viewModelFactory = TrackViewModelFactory(dataSource, application)

        val trackViewModel =
            ViewModelProvider(this, viewModelFactory)[TrackViewModel::class.java]


        binding.trackViewModel = trackViewModel

        val adapter = TrackedActionAdapter()
        binding.actionsList.adapter = adapter

//        adapter.data = listOf(
//            TrackedAction(actionType = "Bath", actionTime = 123),
//            TrackedAction(actionType = "Eat", actionTime = 321),
//            TrackedAction(actionType = "Sleep", actionTime = 444)
//        )


        trackViewModel.actions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })


        binding.lifecycleOwner = this

        trackViewModel.navigateToActionCreation.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController().navigate(R.id.action_trackFragment_to_actionFragment)
                trackViewModel.doneNavigating()
            }
        }


        return binding.root
    }
}