package com.canbazdev.hmskitsproject1.di

import android.content.Context
import com.canbazdev.hmskitsproject1.data.repository.LandMarksRepositoryImpl
import com.canbazdev.hmskitsproject1.data.repository.LoginRepositoryImpl
import com.canbazdev.hmskitsproject1.data.repository.PostsRepositoryImpl
import com.canbazdev.hmskitsproject1.data.repository.RemoteDataSourceImpl
import com.canbazdev.hmskitsproject1.domain.repository.LandMarksRepository
import com.canbazdev.hmskitsproject1.domain.repository.LoginRepository
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.canbazdev.hmskitsproject1.domain.source.RemoteDataSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
*   Created by hamzacanbaz on 7/21/2022
*/
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
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
        @ApplicationContext context: Context
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            providePostsRepository(postsRef),
            provideLoginRepository(context)
        )
    }

    @Provides
    fun providesLandMarksRepository(
        remoteDataSource: RemoteDataSource
    ): LandMarksRepository {
        return LandMarksRepositoryImpl(remoteDataSource)
    }

    @Provides
    fun provideLoginRepository(@ApplicationContext context: Context): LoginRepository =
        LoginRepositoryImpl(context)

}