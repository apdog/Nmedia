package ru.netology.nmedia.post

import android.content.Context
import androidx.core.content.ContextCompat.getString
import ru.netology.nmedia.R
import java.util.Date

//сервис отвечающий за публикацию постов
object WallService {

    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comments>()

    // Переменная для хранения текущего идентификатора поста
    private var startPostsId = 1
    private var startCommentsId = 0
    //метод для создания постов

    //Как он должен работать:
    //добавлять запись в массив, но при этом назначать посту уникальный среди всех постов идентификатор;
    //возвращать пост с уже выставленным идентификатором.
    fun add(post: Post): Post {
        val newPost = post.copy(id = startPostsId++)
        // Инициализируем пустой список комментариев для нового поста
        newPost.comments = mutableListOf()
        posts += newPost
        return newPost
    }

    //метод для обновления постов

    //Как он должен работать:
    //находить среди всех постов запись с тем же id, что и у post и обновлять все свойства;
    //если пост с таким id не найден, то ничего не происходит и возвращается false, в противном случае – возвращается true.
    fun update(post: Post): Boolean {
        val index = posts.indexOfFirst { it.id == post.id }
        return if (index != -1) {
            // Создаем копию обновленного поста
            val updatedPost = post.copy()
            // Заменяем старый пост на обновленный
            posts[index] = updatedPost
            true
        } else {
            false
        }
    }

    fun getPostById(postId: Int): Post? {
        return posts.find { it.id == postId }
    }

    fun createComment(postId: Int, comment: Comments): Comments {
        // проверка существует ли в массиве posts пост с ID равным postId
        val postIndex = posts.indexOfFirst { it.id == postId }
        if (postIndex != -1) {
            // добавить комментарий в массив и возвращаем его
            val newComment = comment.copy(id = posts[postIndex].comments?.size?.plus(1) ?: 0)
            comments += newComment
            posts[postIndex].comments?.add(newComment)
            return newComment
        } else {
            throw PostNotFoundException("Пост с ID $postId не найден")
        }
    }

    fun clearPosts() {
        posts = emptyArray()
        startPostsId = 0
    }

    fun clearComments() {
        comments = emptyArray()
        startCommentsId = 0
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

    fun formatPostDate(date: Date?, context: Context): String {
        if (date == null) return getString(context, R.string.unavailable)
        val now = Date()
        val seconds = (now.time - date.time) / 1000
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

    fun likePost(post: Post): Post {
        post.likedByMe = !post.likedByMe
        if (post.likes == null) {
            post.likes = Likes(count = 1, userLikes = 0, canLike = true)
        } else {
            post.likes?.count = post.likes?.count?.plus(if (post.likedByMe) 1 else -1) ?: 0
        }
        return post
    }

    fun sharePost(post: Post): Post {
        if (post.reposts == null) {
            post.reposts = Reposts(count = 1, userReposted = true)
        } else {
            post.reposts?.count = post.reposts?.count?.plus(100000) ?: 0
        }
        return post
    }

    fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 1100 -> "1 K"
            count < 10000 -> (count / 1000).toString() + " K"
            count < 1_000_000 -> String.format("%.1f K", count / 1000.0)
            else -> String.format("%.1f M", count / 1_000_000.0)
        }
    }

    fun plusView(post: Post): Post {
        if (post.views == null) {
            post.views = Views(count = 1)
        } else {
            post.views?.count = post.views?.count?.plus(100000) ?: 0
        }
        return post
    }

}