package com.example.bambino

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bambino.database.ActionsDatabase
import com.example.bambino.databinding.ActivityMainBinding
import com.example.bambino.track.TrackViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var today: Long = System.currentTimeMillis()


    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = findNavController(R.id.mainNavHost)

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



        datePicker.addOnPositiveButtonClickListener {
            Log.i(
                "ActivityMain", "${datePicker.selection}, ${
                    SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm", Locale.UK)
                        .format(datePicker.selection)
                }"
            )
//            Log.i("ActivityMain", "Between $today and ${today + 86400000}")

            today = datePicker.selection!!
            binding.topAppBar.title = SimpleDateFormat("EEE, MMM d", Locale.UK)
                .format(today).toString()

        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    binding.topAppBar.title = "Home"
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.trackFragment -> {
                    // Respond to navigation item 2 click
                    binding.topAppBar.title = "Track"
                    navController.navigate(R.id.trackFragment)
                    true
                }
                R.id.memoriesFragment -> {
                    // Respond to navigation item 3 click
                    binding.topAppBar.title = "Memories"
                    navController.navigate(R.id.memoriesFragment)
                    true
                }
                else -> false
            }
        }

    }
}
