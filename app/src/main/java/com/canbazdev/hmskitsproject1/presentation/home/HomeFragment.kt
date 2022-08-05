package com.canbazdev.hmskitsproject1.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentHomeBinding
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.canbazdev.hmskitsproject1.util.ActionState
import com.canbazdev.hmskitsproject1.util.OptionsMenu
import com.canbazdev.hmskitsproject1.util.PermissionUtils
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    EasyPermissions.PermissionCallbacks {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAds()
        requestPermissions()
        postsAdapter = PostsAdapter(viewModel)
        binding.rvPosts.adapter = postsAdapter
        binding.adapter = postsAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.postsList.collect {
                postsAdapter.setPostsList(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.actionState.collect {
                when (it) {
                    is ActionState.NavigateTDetailLandmark -> {
                        goToLandmarkDetail(it.post)
                    }
                    is ActionState.NavigateToRegister -> {
                        findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
                    }
                    is ActionState.ShowToast -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        binding.fabOpenPostScreen.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setAds() {
        binding.hwBannerView.setBannerRefresh(60)
        val adParam = AdParam.Builder().build()
        binding.hwBannerView.loadAd(adParam)
        binding.hwBannerView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                println("Ad loaded")
                super.onAdLoaded()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_OK || data == null) {
            return
        }
        if (requestCode == 120) {
            // Input an image for scanning and return the result.
            var obj = data.getParcelableExtra(ScanUtil.RESULT) as HmsScan?
            if (obj != null) {
                // TODO BARKODA ID EKLE DETAIL'E ID GONDER ORADA FIREBASE'DEN CEKME ISLEMI YAP
                println("OBJ ${obj.showResult}")
                val bundle = Bundle()
                bundle.putString("scanUuid", obj.showResult)
                findNavController().navigate(
                    R.id.action_global_landmarkDetailFragment,
                    bundle
                )

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            /* OptionsMenu.LOGOUT.title -> {
                 viewModel.clearUserInfo()
                 AGConnectAuth.getInstance().signOut()
                 viewModel.unableSilentSignIn()
             }*/
            OptionsMenu.SCAN.title -> {
                ScanUtil.startScan(
                    activity, 120, null
                )

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun goToLandmarkDetail(post: Post) {
        val bundle = Bundle()
        bundle.putSerializable("landmark", post)
        if (findNavController().currentDestination?.id == R.id.homeFragment)
            findNavController().navigate(R.id.action_homeFragment_to_landmarkDetailFragment, bundle)

    }


    private fun requestPermissions() {
        if (PermissionUtils.hasScanPermissions(requireContext())) {
            return
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permissions to use this app.",
            PermissionUtils.SCAN_REQUEST_CODE,
            PermissionUtils.CAMERA,
            PermissionUtils.READ_EXTERNAL_STORAGE
        )


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onDestroyView() {
        binding.hwBannerView.destroy()
        super.onDestroyView()
    }

}