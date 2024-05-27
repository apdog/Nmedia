package ru.netology.nmedia.domain

import android.content.Context
import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.post.Comments
import ru.netology.nmedia.domain.post.Post
import java.util.Date

interface PostRepository {
    fun get(): LiveData<Post>
    fun add(post: Post): LiveData<Post>
    fun update(post: Post): LiveData<Boolean>

    // fun createComment(postId: Int, comment: Comments): LiveData<Comments>
    fun clearPosts()
    fun clearComments()
    fun formatPostDate(date: Date?, context: Context): String
    fun likePost(post: Post): LiveData<Post>
    fun sharePost(post: Post): LiveData<Post>
    fun formatCount(count: Int): String
    fun plusView(post: Post): LiveData<Post>
}