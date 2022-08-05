package com.canbazdev.hmskitsproject1.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.canbazdev.hmskitsproject1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.api.AGConnectApi
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.site.api.SearchService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var searchService: SearchService

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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != RESULT_OK || data == null) {
//            return
//        }
//        if (requestCode == 120) {
//            // Input an image for scanning and return the result.
//            var obj = data.getParcelableExtra(ScanUtil.RESULT) as HmsScan?
//            if (obj != null) {
//                // TODO BARKODA ID EKLE DETAIL'E ID GONDER ORADA FIREBASE'DEN CEKME ISLEMI YAP
//                println("OBJ ${obj.showResult}")
//                val bundle = Bundle()
//                bundle.putString("scanUuid", obj.showResult)
//                findNavController(R.id.nav_host_fragment).navigate(
//                    R.id.action_global_landmarkDetailFragment,
//                    bundle
//                )
//
//            }
//        }
//    }


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

    override fun onMapReady(p0: HuaweiMap?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.first {
            it.isVisible
        }.childFragmentManager.fragments[0].onActivityResult(requestCode, resultCode, data)
    }


}