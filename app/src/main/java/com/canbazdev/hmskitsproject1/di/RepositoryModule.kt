package com.canbazdev.hmskitsproject1.di

import android.app.Application
import android.content.Context
import com.canbazdev.hmskitsproject1.data.repository.*
import com.canbazdev.hmskitsproject1.domain.repository.LandMarksRepository
import com.canbazdev.hmskitsproject1.domain.repository.LocationRepository
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.domain.source.LocationDataSource
import com.canbazdev.hmskitsproject1.domain.source.UserDataSource
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
    @Singleton
    fun providePostsRepository(postsRef: CollectionReference): PostsRepository {
        return PostsRepositoryImpl(postsRef)
    }

    @Provides
    fun providePostsRef(
        db: FirebaseFirestore
    ) = db.collection("posts")


    @Provides
    fun providesUserRemoteDataSource(
        postsRef: CollectionReference,
        @ApplicationContext context: Context,
        application: Application,
    ): UserDataSource {
        return UserDataSourceImpl(
            providePostsRepository(postsRef),
            provideLoginRepository(context),
            provideLocationRepository(application)
        )
    }

    @Provides
    fun providesLocationRemoteDataSource(
        application: Application
    ): LocationDataSource {
        return LocationDataSourceImpl(provideLocationRepository(application))
    }

    @Provides
    fun providesLandMarksRepository(
        remoteDataSource: UserDataSource
    ): LandMarksRepository {
        return LandMarksRepositoryImpl(remoteDataSource)
    }

    @Provides
    fun provideLocationRepository(
        application: Application
    ): LocationRepository {
        return LocationRepositoryImpl(application, getSearchService(application.applicationContext))
    }

    @Provides
    fun provideLoginRepository(@ApplicationContext context: Context): LoginRepository =
        LoginRepositoryImpl(context)

}