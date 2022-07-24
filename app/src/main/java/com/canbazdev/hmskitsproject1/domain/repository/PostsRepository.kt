package com.canbazdev.hmskitsproject1.domain.repository

import com.canbazdev.hmskitsproject1.domain.model.Post
import com.canbazdev.hmskitsproject1.util.Work

/*
*   Created by hamzacanbaz on 7/21/2022
*/
interface PostsRepository {
    fun addPostToFirestore(post: Post): Work<Post>
}