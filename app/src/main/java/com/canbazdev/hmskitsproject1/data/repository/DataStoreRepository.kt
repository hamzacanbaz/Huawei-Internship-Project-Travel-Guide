package com.canbazdev.hmskitsproject1.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/*
*   Created by hamzacanbaz on 7/20/2022
*/
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("USER_PREFERENCES")

@Singleton
class DataStoreRepository @Inject constructor(@ApplicationContext val context: Context) {

    private val isOpenedFirstTime = booleanPreferencesKey("IS_OPENED_FIRST_TIME")
    private val charactersLayoutManager = intPreferencesKey("CHARACTERS_LAYOUT_MANAGER")


    val getOpenedFirstTime: Flow<Boolean> = context.dataStore.data.map {
        it[isOpenedFirstTime] ?: false
    }

    suspend fun setOpenedFirstTime(isOpened: Boolean) {
        context.dataStore.edit {
            it[isOpenedFirstTime] = isOpened
        }
    }




}
