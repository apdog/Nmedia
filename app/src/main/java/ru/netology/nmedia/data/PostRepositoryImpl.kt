package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.domain.post.Likes
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.post.Reposts
import ru.netology.nmedia.domain.post.Views
import ru.netology.nmedia.domain.PostRepository
import java.util.Date

object PostRepositoryImpl : PostRepository {

    // Создание новой даты для поста приват
    val newDate = Date(System.currentTimeMillis() - (360 * 60 * 1000))
    private var post = Post(
        id = 0, fromId = 0, date = newDate,
        title = "Нетология",
        text = "Нейросети восстанут против дизайнеров? Сомнительно, хоть это и пугает. Чтобы избавиться от страха, превратите их в союзников. Предлагаем открыть Midjourney и пошагово сгенерировать изображение по инструкции от Андрея Малеваника, автора и преподавателя курса «Нейросети для дизайна».Смотрите подробности о курсе и записывайтесь на бесплатную консультацию по программе обучения: https://netolo.gy/c8Mg",
        friendsOnly = false,
        comments = mutableListOf(),
        likes = Likes(count = 0, userLikes = 0, canLike = true),
        likedByMe = false,
        isPinned = false,
        reposts = Reposts(count = 0, false),
        views = Views(count = 0),
        attachments = null
    )

    private val liveData = MutableLiveData(post)
    override fun get(): LiveData<Post> = liveData

    override fun likePost() {
        post = post.copy(
            likedByMe = !post.likedByMe,
            likes = if (post.likes == null) {
                Likes(count = 1, userLikes = 0, canLike = true)
            } else {
                Likes(
                    count = post.likes?.count?.plus(if (post.likedByMe) -1 else 1) ?: 0,
                    userLikes = post.likes?.userLikes?.plus(if (post.likedByMe) -1 else 1) ?: 0,
                    canLike = true
                )
            }
        )
        liveData.value = post
    }

    override fun sharePost() {
        post = post.copy(
            reposts = Reposts(count = post.reposts?.count?.plus(1) ?: 1, userReposted = true),
            // временно, пока не пойму как сделать это через активити
            views = Views(count = post.views?.count?.plus(1) ?: 1)
        )
        liveData.value = post
    }

    override fun plusView() {
        post = post.copy(
            views = Views(count = post.views?.count?.plus(1) ?: 1)
        )
        liveData.value = post
    }
}