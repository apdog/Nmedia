package ru.netology.nmedia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.post.attachments.Attachment

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: Long,
    val date: Long,
    val title: String,
    val content: String?,
    val friendsOnly: Boolean,
    val comments: Int = 0,
    val likes: Int,
    val likedByMe: Boolean = false,
    val reposts: Int,
    val views: Int,
    val isPinned: Boolean,
    val attachments: List<Attachment>?
) {
    fun toDto(): Post = Post(
        id = id,
        author = author,
        date = date,
        title = title,
        content = content,
        friendsOnly = friendsOnly,
        comments = comments,
        likes = likes,
        likedByMe = likedByMe,
        reposts = reposts,
        views = views,
        isPinned = isPinned,
        attachments = attachments
    )

    companion object {
        fun fromDto(post: Post): PostEntity = with(post) {
            PostEntity(
                id,
                author,
                date,
                title = title.ifBlank { "Нетология" },
                content,
                friendsOnly,
                comments,
                likes,
                likedByMe,
                reposts,
                views,
                isPinned,
                attachments
            )
        }
    }
}
