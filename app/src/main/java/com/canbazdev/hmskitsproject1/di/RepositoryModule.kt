package com.canbazdev.hmskitsproject1.di

import com.canbazdev.hmskitsproject1.data.repository.PostsRepositoryImpl
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
*   Created by hamzacanbaz on 7/21/2022

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
}*/