package ru.netology.nmedia.domain

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.post.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likePost(id: Int)
    fun sharePost(id: Int)
    fun plusView(id: Int)
}