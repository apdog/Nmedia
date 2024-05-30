package ru.netology.nmedia.domain

import android.content.Context
import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.post.Comments
import ru.netology.nmedia.domain.post.Post
import java.util.Date

interface PostRepository {
    fun get(): LiveData<Post>
    fun likePost()
    fun sharePost()
    fun plusView()
    fun formatCount(count: Int): String
    fun formatPostDate(post: Post, context: Context): String
}