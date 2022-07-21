package com.canbazdev.hmskitsproject1.presentation

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.canbazdev.hmskitsproject1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.api.AGConnectApi
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            AGConnectInstance.initialize(applicationContext);
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
                // R.id.homeFragment -> bottomNavigationView.visibility = View.GONE


                else -> bottomNavigationView.visibility = View.VISIBLE

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
                R.id.profileFragment
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}