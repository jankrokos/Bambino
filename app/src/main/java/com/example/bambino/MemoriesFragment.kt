package com.example.bambino

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bambino.databinding.FragmentMemoriesBinding

class MemoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMemoriesBinding.inflate(inflater, container, false)
        return binding.root
    }
}