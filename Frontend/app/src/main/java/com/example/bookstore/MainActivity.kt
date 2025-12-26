package com.example.bookstore

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookstore.databinding.ActivityMainBinding
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        val navController = navHostFragment.navController
        //---------------DISABLE BELOW PART FOR QUICK TEST
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val bottomNavView = findViewById<BottomNavigationView>(R.id.nav_view)

            when (destination.id) {
                R.id.navigation_login,
                R.id.navigation_register -> bottomNavView.visibility = View.GONE

                else -> bottomNavView.visibility = View.VISIBLE
            }
        }
        //---------------DISABLE ABOVE PART FOR QUICK TEST
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_cart,
                R.id.navigation_order,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}