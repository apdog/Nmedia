package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.domain.post.Likes
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.post.Reposts
import ru.netology.nmedia.domain.post.Views
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.presentation.rv.PostListAdapter
import java.util.Date

object PostRepositoryImpl : PostRepository {

    // Создание новой даты для поста приват
    private val newDate = Date(System.currentTimeMillis() - (360 * 60 * 1000))

    private var listOfPosts = listOf(
        Post(
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
        ), Post(
            id = 1, fromId = 0, date = newDate,
            title = "Нетология 2",
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
    )

    private val liveData = MutableLiveData(listOfPosts)
    override fun get(): LiveData<List<Post>> = liveData

    override fun likePost(id: Int) {
        listOfPosts = listOfPosts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likes == null) {
                    Likes(count = 1, userLikes = 0, canLike = true)
                } else {
                    Likes(
                        count = it.likes.count.plus(if (it.likedByMe) -1 else 1)
                            ?: 0,
                        userLikes = it.likes.userLikes.plus(if (it.likedByMe) -1 else 1)
                            ?: 0,
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
                    count = it.reposts?.count?.plus(1) ?: 1,
                    userReposted = true
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
}