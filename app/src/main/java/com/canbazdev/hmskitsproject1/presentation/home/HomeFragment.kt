package com.canbazdev.hmskitsproject1.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.databinding.FragmentHomeBinding
import com.canbazdev.hmskitsproject1.domain.model.Post
import com.canbazdev.hmskitsproject1.presentation.base.BaseFragment
import com.canbazdev.hmskitsproject1.util.ActionState
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.service.AccountAuthService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var mAuthParam: AccountAuthParams
    private lateinit var mAuthService: AccountAuthService


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        postsAdapter = PostsAdapter(viewModel)
        /*  postsAdapter.setPostsList(
              listOf(
                  Post(landmarkName = "divrigi ulu cami", landmarkImage = ""),
                  Post(landmarkInfo = "ahahhahaa", landmarkImage = "")
              )
          )*/
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
                    else -> {}
                }
            }
        }


        binding.fabOpenPostScreen.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postFragment)
        }
        binding.btnSignOut.setOnClickListener {
            AGConnectAuth.getInstance().signOut()
            viewModel.unableSilentSignIn()
            findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
        }

        /*mAuthParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .createParams()

        mAuthService = AccountAuthManager.getService(activity, mAuthParam)

        binding.btnSignOut.setOnClickListener {
            signOut()
        }*/
        super.onViewCreated(view, savedInstanceState)
    }

    /* private fun signOut() {
         val signOutTask = mAuthService.signOut()
         signOutTask.addOnSuccessListener {
             println("success")
             binding.status.text = "Logged Out"
         }.addOnFailureListener {
             println("error")
         }
     }*/

    private fun goToLandmarkDetail( post: Post) {
        val bundle = Bundle()
        bundle.putSerializable("landmark",post)
        if (findNavController().currentDestination?.id==R.id.homeFragment)
        findNavController().navigate(R.id.action_homeFragment_to_landmarkDetailFragment,bundle)

    }
}