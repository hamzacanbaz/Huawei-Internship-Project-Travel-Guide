package com.canbazdev.hmskitsproject1.data.source.remote.common

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase


// *   Created by hamzacanbaz on 7/21/2022

class FirebaseDbManager {
    //internal val database = Firebase.database
    //internal val storage = Firebase.storage(FIRE_BASE_STORAGE)
    val fireStore = Firebase.firestore
    //internal val functions = Firebase.functions

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        fireStore.firestoreSettings = settings

    }

    companion object {
        fun initFirebase(context: Context) {
            FirebaseApp.initializeApp(context)
        }
    }

    fun controlStorageUrl(url: String): Boolean {
        if (url.contains(".png") || url.contains(".mp3") || url.contains(".mp4") || url.contains("/video") || url.contains(
                "/audio"
            ) || url.contains("/image")
        ) {
            return true
        }
        return false
    }

    fun gatherStorageUrlExtension(url: String): String {
        if (url.contains(".png") || url.contains(".mp3") || url.contains(".mp4")) {
            return url.substringAfterLast(".")
        }
        return url.substringAfterLast("/").substringBefore("%")
    }


}