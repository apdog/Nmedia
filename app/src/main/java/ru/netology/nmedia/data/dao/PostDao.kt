package ru.netology.nmedia.data.dao

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.post.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Int)
    fun removeById(id: Int)
    fun sharePost(id: Int)
    fun plusView(id: Int)
}