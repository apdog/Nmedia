package ru.netology.nmedia.data

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.domain.post.Comments
import ru.netology.nmedia.domain.post.Likes
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.post.PostNotFoundException
import ru.netology.nmedia.domain.post.Reposts
import ru.netology.nmedia.domain.post.Views
import ru.netology.nmedia.domain.PostRepository
import java.util.Date

object PostRepositoryImpl : PostRepository {

    private var posts = mutableListOf<Post>()
    private var comments = mutableListOf<Comments>()

    // Переменная для хранения текущего идентификатора поста
    private var startPostsId = 1
    private var startCommentsId = 0

    //метод для создания постов

    //Как он должен работать:
    //добавлять запись в массив, но при этом назначать посту уникальный среди всех постов идентификатор;
    //возвращать пост с уже выставленным идентификатором.
    override fun add(post: Post): LiveData<Post> {
        //Создание копии поста с новым ID и инициализацией пустого списка комментариев
        val newPost = post.copy(id = startPostsId++, comments = mutableListOf())
        //Добавление нового поста в список
        posts += newPost
        //Создание и возврат LiveData<Post>
        val result = MutableLiveData<Post>()
        result.value = newPost
        return result
    }

    //метод для обновления постов
    //Как он должен работать:
    //находить среди всех постов запись с тем же id, что и у post и обновлять все свойства;
    //если пост с таким id не найден, то ничего не происходит и возвращается false,
    // в противном случае – возвращается true.
    override fun update(post: Post): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            // Создаем копию обновленного поста
            val updatedPost = post.copy()
            // Заменяем старый пост на обновленный
            posts[index] = updatedPost
            result.value = true // Операция обновления выполнена успешно
        } else {
            result.value = false // Пост с указанным ID не найден, операция не выполнена
        }
        return result
    }

    override fun getPostById(postId: Int): LiveData<Post?> {
        val result = MutableLiveData<Post?>()
        result.value = posts.find { it.id == postId }
        return result
    }

    override fun createComment(postId: Int, comment: Comments): LiveData<Comments> {
        val result = MutableLiveData<Comments>()
        // проверка существует ли в массиве posts пост с ID равным postId
        val postIndex = posts.indexOfFirst { it.id == postId }
        if (postIndex != -1) {
            // добавить комментарий в массив и возвращаем его
            val newComment = comment.copy(id = posts[postIndex].comments?.size?.plus(1) ?: 0)
            comments += newComment
            posts[postIndex].comments?.add(newComment)
            result.value = newComment
        } else {
            throw PostNotFoundException("Пост с ID $postId не найден")
        }
        return result
    }

    override fun clearPosts() {
        posts = mutableListOf()
        startPostsId = 0
    }

    override fun clearComments() {
        comments = mutableListOf()
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

    override fun formatPostDate(date: Date?, context: Context): String {
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

    override fun likePost(post: Post): LiveData<Post> {
        val result = MutableLiveData<Post>()
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            val updatedPost = post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likes == null) {
                    Likes(count = 1, userLikes = 0, canLike = true)
                } else {
                    Likes(count = post.likes.count + if (post.likedByMe) -1 else 1, userLikes = post.likes.userLikes, canLike = true)
                }
            )
            posts[index] = updatedPost
            result.value = updatedPost
        }
        return result
    }

    override fun sharePost(post: Post): LiveData<Post> {
        val result = MutableLiveData<Post>()
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            val updatedPost = post.copy(
                reposts = Reposts(count = post.reposts?.count?.plus(1) ?: 1, userReposted = true),
                // временно, пока не пойму как сделать это через активити
                views = Views(count = post.views?.count?.plus(1) ?: 1)
            )
            posts[index] = updatedPost
            result.value = updatedPost
        }
        return result
    }

    override fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10_000 -> "${(count / 1000.0 * 10).toInt() / 10.0}K"
            count < 1_000_000 -> "${(count / 1000)}K"
            else -> "${(count / 1_000_000.0 * 10).toInt() / 10.0}M"
        }
    }

    override fun plusView(post: Post): LiveData<Post> {
        val result = MutableLiveData<Post>()
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            val updatedPost = post.copy(
                views = Views(count = post.views?.count?.plus(1) ?: 1)
            )
            posts[index] = updatedPost
            result.value = updatedPost
        }
        return result
    }

}