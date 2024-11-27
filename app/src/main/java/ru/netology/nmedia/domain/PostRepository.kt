package ru.netology.nmedia.domain

import ru.netology.nmedia.domain.post.Post

interface PostRepository {
    fun getAllAsync(callback: PostCallback<List<Post>>)
    fun likeById(id: Long,callback: PostCallback<Post>)
    fun removeById(id: Long, callback: PostCallback<Unit>)
    fun save(post: Post, callback: PostCallback<Post>)
//    fun sharePost(id: Long)
//    fun plusView(id: Long)
    interface PostCallback<T> {
        fun onSuccess(result: T)
        fun onError(error: Throwable)
    }
}