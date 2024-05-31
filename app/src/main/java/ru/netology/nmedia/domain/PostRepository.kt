package ru.netology.nmedia.domain

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.post.Post

interface PostRepository {
    fun get(): LiveData<Post>
    fun likePost()
    fun sharePost()
    fun plusView()
}