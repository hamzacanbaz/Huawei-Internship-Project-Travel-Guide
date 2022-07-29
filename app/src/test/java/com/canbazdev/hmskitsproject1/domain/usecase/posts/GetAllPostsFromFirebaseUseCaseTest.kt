package com.canbazdev.hmskitsproject1.domain.usecase.posts

import com.canbazdev.hmskitsproject1.data.repository.PostsRepositoryImpl
import com.canbazdev.hmskitsproject1.domain.repository.PostsRepository
import org.junit.Before


/*
*   Created by hamzacanbaz on 7/27/2022
*/
class GetAllPostsFromFirebaseUseCaseTest {
    private lateinit var postsRepository: PostsRepository
    private lateinit var insertPostUseCase: InsertPostUseCase

    @Before
    fun setUp() {
       // insertPostUseCase = InsertPostUseCase()
        //postsRepository = PostsRepositoryImpl()
    }

}