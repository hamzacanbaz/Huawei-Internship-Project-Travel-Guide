package com.canbazdev.hmskitsproject1.di

import android.app.Application
import android.content.Context
import com.canbazdev.hmskitsproject1.data.repository.*
import com.canbazdev.hmskitsproject1.data.source.remote.AccessTokenService
import com.canbazdev.hmskitsproject1.data.source.remote.LandmarkService
import com.canbazdev.hmskitsproject1.domain.repository.LocationRepository
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.domain.repository.NotificationRepository
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.net.URLEncoder
import javax.inject.Singleton

/*
*   Created by hamzacanbaz on 7/21/2022
*/
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun getSearchService(context: Context): SearchService {
        return SearchServiceFactory.create(
            context, URLEncoder.encode(
                "DAEDAJucCbh6nmVXWf3K0R+u79eN2LIjyT3qudeHBb6KSM4QiehF/sGVWF8don02ZWlnLc2l9nXxqZufAalXUHzLYRnZRe9Vsgaa4Q==",
                "utf-8"
            )
        )
    }


    @Provides
    fun providePostsRepository(
        postsRef: CollectionReference,
        db: FirebaseFirestore
    ): PostsRepository {
        return PostsRepositoryImpl(postsRef, db)
    }

    @Provides
    fun providePostsRef(
        db: FirebaseFirestore
    ) = db.collection("posts")


    @Provides
    fun providesUserRemoteDataSource(
        postsRef: CollectionReference,
        db: FirebaseFirestore,
        @ApplicationContext context: Context,
        application: Application
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            providePostsRepository(postsRef, db),
            provideLoginRepository(context, db),
            provideLocationRepository(application)
        )
    }


    @Provides
    fun provideLocationRepository(
        application: Application
    ): LocationRepository {
        return LocationRepositoryImpl(application, getSearchService(application.applicationContext))
    }

    @Provides
    fun provideLoginRepository(
        @ApplicationContext context: Context,
        db: FirebaseFirestore
    ): LoginRepository =
        LoginRepositoryImpl(context, db)

    @Provides
    fun provideNotificationRepository(
        landmarkService: LandmarkService,
        accessTokenService: AccessTokenService
    ): NotificationRepository {
        return NotificationRepositoryImpl(landmarkService, accessTokenService)
    }

}