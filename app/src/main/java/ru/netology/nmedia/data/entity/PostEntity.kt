package ru.netology.nmedia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.domain.post.Attachment
import ru.netology.nmedia.domain.post.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String?,
    val content: String?,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val attachment: Attachment?,
    val title: String,
    val authorAvatar: String? = null
) {
    fun toDto(): Post = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        attachment = attachment,
        title = title,
        authorAvatar = authorAvatar
//        friendsOnly = friendsOnly,
//        comments = comments,
//        reposts = reposts,
//        views = views,
//        isPinned = isPinned,
    )

    companion object {
        fun fromDto(post: Post): PostEntity = with(post) {
            PostEntity(
                id,
                author,
                content,
                published,
                likedByMe,
                likes,
                attachment,
                title = title.ifBlank { "Нетология" },
//                friendsOnly,
//                comments,
//                reposts,
//                views,
//                isPinned,
                        )
        }
    }
}
