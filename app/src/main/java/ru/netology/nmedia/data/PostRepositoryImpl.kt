package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.domain.post.Likes
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.post.Reposts
import ru.netology.nmedia.domain.post.Views
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.attachments.Video
import ru.netology.nmedia.domain.post.attachments.VideoAttachment
import java.util.Date

object PostRepositoryImpl : PostRepository {

    private var newDate = Date(System.currentTimeMillis() - (360 * 60 * 1000))

    private var nextId = 3

    private var listOfPosts = listOf(
        Post(
            id = 1,
            fromId = 0,
            date = newDate,
            title = "Нетология",
            text = "Нейросети восстанут против дизайнеров? Сомнительно, хоть это и пугает. Чтобы избавиться от страха, превратите их в союзников. Предлагаем открыть Midjourney и пошагово сгенерировать изображение по инструкции от Андрея Малеваника, автора и преподавателя курса «Нейросети для дизайна».Смотрите подробности о курсе и записывайтесь на бесплатную консультацию по программе обучения: https://netolo.gy/c8Mg",
            friendsOnly = false,
            comments = mutableListOf(),
            likes = Likes(count = 251, userLikes = 0, canLike = true),
            likedByMe = false,
            isPinned = false,
            reposts = Reposts(count = 48, false),
            views = Views(count = 0),
            attachments = listOf(
                VideoAttachment(
                    Video(
                        id = 1,
                        ownerId = 0,
                        title = "Очень важное видео",
                        description = "Смешные животные",
                        duration = 300,
                        image = R.drawable.placeholder_img,
                        url = "xbks1QDEWNM&t=43s"
                    )
                )
            )
        ), Post(
            id = 2,
            fromId = 0,
            date = newDate,
            title = "Нетология",
            text = "Предлагаем открыть Midjourney и пошагово сгенерировать изображение по инструкции от Андрея Малеваника, автора и преподавателя курса «Нейросети для дизайна».",
            friendsOnly = false,
            comments = mutableListOf(),
            likes = Likes(count = 35, userLikes = 0, canLike = true),
            likedByMe = false,
            isPinned = false,
            reposts = Reposts(count = 10, false),
            views = Views(count = 0),
            attachments = null
        )
    )

    private val liveData = MutableLiveData(listOfPosts)
    override fun get(): LiveData<List<Post>> = liveData

    override fun likePost(id: Int) {
        listOfPosts = listOfPosts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe, likes = if (it.likes == null) {
                    Likes(count = 1, userLikes = 0, canLike = true)
                } else {
                    Likes(
                        count = it.likes.count.plus(if (it.likedByMe) -1 else 1) ?: 0,
                        userLikes = it.likes.userLikes.plus(if (it.likedByMe) -1 else 1) ?: 0,
                        canLike = true
                    )
                }
            )
        }
        liveData.value = listOfPosts
    }

    override fun sharePost(id: Int) {
        listOfPosts = listOfPosts.map {
            if (it.id != id) it else it.copy(
                reposts = Reposts(
                    count = it.reposts?.count?.plus(1) ?: 1, userReposted = true
                ),
                // временно, пока не пойму как сделать это через активити
                views = Views(count = it.views?.count?.plus(1) ?: 1)
            )
        }
        liveData.value = listOfPosts
    }

    override fun plusView(id: Int) {
        listOfPosts = listOfPosts.map {
            if (it.id != id) it else it.copy(
                views = Views(count = it.views?.count?.plus(1) ?: 1)
            )
        }
        liveData.value = listOfPosts
    }

    override fun removeById(id: Int) {
        listOfPosts = listOfPosts.filter { it.id != id }
        liveData.value = listOfPosts
    }

    override fun save(post: Post) {
        if (post.id == 0) {
            listOfPosts = listOf(
                post.copy(
                    id = nextId++, title = "Нетология", date = Date(), likedByMe = false, comments = mutableListOf()
                )
            ) + listOfPosts
            liveData.value = listOfPosts
            return
        }
        update(post)

        listOfPosts = listOfPosts.map {
            if (it.id != post.id) it else it.copy(text = post.text)
        }
        liveData.value = listOfPosts
    }

    private fun update(post: Post) {
        listOfPosts = listOfPosts.map {
            if (it.id != post.id) it else post
        }.toMutableList()
        liveData.value = listOfPosts
    }
}