package com.example.bambino.action

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bambino.R
import com.example.bambino.database.ActionsDatabase
import com.example.bambino.databinding.FragmentActionBinding

class ActionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentActionBinding.inflate(inflater, container, false)

//        val application = requireNotNull(this.activity).application
//        val dataSource = ActionsDatabase.getInstance(application).actionsDatabaseDao
//
//        val arguments = ActionFragmentArgs.fromBundle(requireArguments())

//
//        val viewModelFactory = ActionViewModelFactory(arguments.trackedActionKey, dataSource)
//
//        val actionViewModel =
//            ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]
//
//        binding.actionViewModel = actionViewModel
//
//        actionViewModel.navigateToTrackList.observe(viewLifecycleOwner){
//            if (it){
//                this.findNavController().navigate(R.id.action_actionFragment_to_trackFragment)
//                actionViewModel.doneNavigating()
//            }
//        }

        binding.addActivityButton.setOnClickListener{
            this.findNavController().navigate(R.id.action_actionFragment_to_trackFragment)
        }


        return binding.root
    }
}