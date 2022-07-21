package com.canbazdev.hmskitsproject1.presentation.home

import androidx.lifecycle.ViewModel
import com.canbazdev.hmskitsproject1.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: DataStoreRepository
) : ViewModel() {

}