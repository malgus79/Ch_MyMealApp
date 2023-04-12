package com.mymealapp.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.mymealapp.R
import com.mymealapp.core.showConnectivitySnackbar
import com.mymealapp.databinding.ActivityMainBinding
import com.mymealapp.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var doubleBackToExitPressedOnce = false

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        checkNetworkConnection()
    }

    private fun setupNavigation() {
        val navView = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            val hideNavViewDestinations = listOf(
                R.id.mealDetailFragment,
                R.id.mealDetailByCategoryFragment,
                R.id.mealByCategoryFragment
            )
            navView.visibility = if (nd.id in hideNavViewDestinations) View.GONE else View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()

    }

    private fun checkNetworkConnection() {
        lifecycleScope.launchWhenCreated {
            viewModel.isConnected
                .distinctUntilChanged()
                .drop(1)
                .collect { isConnected ->
                    when (isConnected) {
                        true -> showConnectivitySnackbar(true)
                        false -> showConnectivitySnackbar(false)
                    }
                }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            onBackPressedDispatcher.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) is HomeFragment) {
            Snackbar.make(binding.root, R.string.press_again, Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.bottomNavigationView)
                .show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            doubleBackToExitPressedOnce = false
            onBackPressedDispatcher.onBackPressed()
        }
    }
}