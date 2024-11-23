package ru.netology.nmedia.domain

import ru.netology.nmedia.domain.post.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long): Post
    fun sharePost(id: Long)
    fun plusView(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}