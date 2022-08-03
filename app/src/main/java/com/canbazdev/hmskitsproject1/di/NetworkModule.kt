package com.canbazdev.hmskitsproject1.di

import com.canbazdev.hmskitsproject1.data.source.remote.AccessTokenService
import com.canbazdev.hmskitsproject1.data.source.remote.LandmarkService
import com.canbazdev.hmskitsproject1.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
*   Created by hamzacanbaz on 8/1/2022
*/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNotificationService(): LandmarkService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LandmarkService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenService(): AccessTokenService {
        return Retrofit.Builder()
            .baseUrl("https://oauth-login.cloud.huawei.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccessTokenService::class.java)
    }
}
