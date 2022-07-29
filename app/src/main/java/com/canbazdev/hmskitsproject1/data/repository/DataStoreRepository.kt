package com.canbazdev.hmskitsproject1.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
    private val silentSignInEnabled = booleanPreferencesKey("SILENT_SIGNIN_ENABLED")
    private val currentUserId = stringPreferencesKey("CURRENT_USER_ID")
    private val currentUserEmail = stringPreferencesKey("CURRENT_USER_EMAIL")
    private val currentUserName = stringPreferencesKey("CURRENT_USER_NAME")


    val getCurrentUserId: Flow<String> = context.dataStore.data.map {
        it[currentUserId] ?: ""
    }

    suspend fun setCurrentUserId(userId: String) {
        context.dataStore.edit {
            it[currentUserId] = userId
        }
    }

    val getCurrentUserEmail: Flow<String> = context.dataStore.data.map {
        it[currentUserEmail] ?: ""
    }

    suspend fun setCurrentUserEmail(userEmail: String) {
        context.dataStore.edit {
            it[currentUserEmail] = userEmail
        }
    }

    val getSilentSignInEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[silentSignInEnabled] ?: false
    }

    fun getEnabled(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[silentSignInEnabled] ?: false
        }
    }

    suspend fun setSilentSignInEnabled(isEnabled: Boolean) {
        context.dataStore.edit {
            it[silentSignInEnabled] = isEnabled
        }
    }

    val getOpenedFirstTime: Flow<Boolean> = context.dataStore.data.map {

        it[isOpenedFirstTime] ?: false
    }

    suspend fun setOpenedFirstTime(isOpened: Boolean) {
        context.dataStore.edit {
            it[isOpenedFirstTime] = isOpened
        }
    }


}
