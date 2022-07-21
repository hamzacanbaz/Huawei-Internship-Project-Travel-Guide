package com.canbazdev.hmskitsproject1.domain.repository

import com.canbazdev.hmskitsproject1.domain.model.Post
import com.canbazdev.hmskitsproject1.util.Resource
import kotlinx.coroutines.flow.Flow

/*
*   Created by hamzacanbaz on 7/21/2022
*/
interface PostsRepository {
    suspend fun addPostToFirestore(text: String, image: String): Flow<Resource<Post>>
}