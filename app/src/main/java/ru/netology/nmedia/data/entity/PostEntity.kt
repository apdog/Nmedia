package ru.netology.nmedia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.post.attachments.Attachment
import java.util.Date

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val fromId: Long,
    val date: Date,
    val title: String,
    val text: String?,
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
        fromId = fromId,
        date = date,
        title = title,
        text = text,
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
                fromId,
                date,
                title = title.ifBlank { "Нетология" },
                text,
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
