package com.canbazdev.hmskitsproject1.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.canbazdev.hmskitsproject1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.api.AGConnectApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavbar)
        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navigationHost.findNavController()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        bottomNavigationView.inflateMenu(R.menu.bottom_navbar)
        setUpBottomNavigationView(navController, bottomNavigationView, toolbar)
        setUpToolbar(navController, toolbar)

        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(applicationContext)
        }
        AGConnectApi.getInstance().activityLifecycle().onCreate(this)
    }


    private fun setUpBottomNavigationView(
        navController: NavController,
        bottomNavigationView: BottomNavigationView,
        toolbar: Toolbar
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.registerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }

                R.id.loginFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }

                R.id.splashFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    toolbar.visibility = View.GONE
                }


                else -> {
                    toolbar.visibility = View.VISIBLE
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun setUpToolbar(navController: NavController, toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.homeFragment,
                R.id.mapFragment,
                R.id.profileFragment,
                R.id.registerFragment,
                R.id.loginFragment
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.first {
            it.isVisible
        }.childFragmentManager.fragments[0].onActivityResult(requestCode, resultCode, data)
    }


}