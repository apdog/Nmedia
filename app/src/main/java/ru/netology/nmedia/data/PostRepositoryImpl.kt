package ru.netology.nmedia.data

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
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

    override fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10_000 -> "${(count / 1000.0 * 10).toInt() / 10.0}K"
            count < 1_000_000 -> "${(count / 1000)}K"
            else -> "${(count / 1_000_000.0 * 10).toInt() / 10.0}M"
        }
    }

    override fun formatPostDate(post: Post, context: Context): String {
        val now = Date()
        val seconds = (now.time - post.date.time) / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = weeks / 4
        val years = months / 12
        val resources = context.resources ?: return getString(context, R.string.unavailable)

        return when {
            years > 0 -> formatNumberWithSuffix(
                years, resources.getString(R.string.one_year),
                resources.getString(R.string.few_years), resources.getString(R.string.many_years)
            )

            months > 0 -> formatNumberWithSuffix(
                months, resources.getString(R.string.one_month),
                resources.getString(R.string.few_months), resources.getString(R.string.many_months)
            )

            weeks > 0 -> formatNumberWithSuffix(
                weeks, resources.getString(R.string.one_week),
                resources.getString(R.string.few_weeks), resources.getString(R.string.many_weeks)
            )

            days > 0 -> formatNumberWithSuffix(
                days, resources.getString(R.string.one_day),
                resources.getString(R.string.few_days), resources.getString(R.string.many_days)
            )

            hours > 0 -> formatNumberWithSuffix(
                hours, resources.getString(R.string.one_hour),
                resources.getString(R.string.few_hours), resources.getString(R.string.many_hours)
            )

            minutes > 0 -> formatNumberWithSuffix(
                minutes,
                resources.getString(R.string.one_minute),
                resources.getString(R.string.few_minutes),
                resources.getString(R.string.many_minutes)
            )

            else -> resources.getString(R.string.just_now)
        }
    }

    private fun formatNumberWithSuffix(
        number: Long,
        one: String,
        few: String,
        many: String
    ): String {
        return when {
            number % 10 == 1L && number % 100 != 11L -> "$number $one"
            number % 10 in 2..4 && number % 100 !in 12..14 -> "$number $few"
            else -> "$number $many"
        }
    }
}