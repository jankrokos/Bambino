package com.example.bambino

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.example.bambino.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = findNavController(R.id.mainNavHost)


        //BOTTOM NAVIGATION
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
