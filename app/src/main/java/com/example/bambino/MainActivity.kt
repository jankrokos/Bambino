package com.example.bambino

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bambino.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = findNavController(R.id.mainNavHost)
        binding.bottomNavigation.setupWithNavController(navController)


        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.calendar -> {
                    datePicker.show(supportFragmentManager, "tag")
                    true
                }
                R.id.settings -> {
                    // Handle favorite icon press
                    true
                }
                else -> false
            }
        }


//        binding.bottomNavigation.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.homeFragment -> {
//                    binding.topAppBar.title = "Home"
//                    true
//                }
//                R.id.trackFragment -> {
//                    // Respond to navigation item 2 click
//                    binding.topAppBar.title = "Track"
//                    true
//                }
//                R.id.memoriesFragment -> {
//                    // Respond to navigation item 3 click
//                    binding.topAppBar.title = "Memories"
//                    true
//                }
//                else -> false
//            }
//        }

    }
}
