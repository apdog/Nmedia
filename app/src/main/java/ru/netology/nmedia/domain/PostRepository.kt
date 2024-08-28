package ru.netology.nmedia.domain

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.post.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likePost(id: Long)
    fun sharePost(id: Long)
    fun plusView(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}