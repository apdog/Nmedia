package ru.netology.nmedia.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.domain.post.Likes
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.post.Reposts
import ru.netology.nmedia.domain.post.Views
import ru.netology.nmedia.domain.PostRepository
import java.util.Date

class PostRepositoryFileImpl(
    private val context: Context
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "posts.json"
    private var newDate = Date(System.currentTimeMillis() - (360 * 60 * 1000))
    private var nextId = 1
    private var listOfPosts = emptyList<Post>()
    private val liveData = MutableLiveData(listOfPosts)

    override fun get(): LiveData<List<Post>> = liveData

    init {
        val file = context.filesDir.resolve(fileName)
        if (file.exists()) {
            context.openFileInput(fileName).bufferedReader().use {
                listOfPosts = gson.fromJson(it, type)
                nextId = listOfPosts.maxOfOrNull { it.id }?.inc() ?: 1
                liveData.value = listOfPosts
            }
        } else {
            sync()
        }
    }

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
        sync()
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
        sync()
    }

    override fun plusView(id: Int) {
        listOfPosts = listOfPosts.map {
            if (it.id != id) it else it.copy(
                views = Views(count = it.views?.count?.plus(1) ?: 1)
            )
        }
        liveData.value = listOfPosts
        sync()
    }

    override fun removeById(id: Int) {
        listOfPosts = listOfPosts.filter { it.id != id }
        liveData.value = listOfPosts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0) {
            listOfPosts = listOf(
                post.copy(
                    id = nextId++,
                    title = "Нетология",
                    date = Date(),
                    likedByMe = false,
                    comments = mutableListOf()
                )
            ) + listOfPosts
            liveData.value = listOfPosts
            sync()
            return
        }
        update(post)
        listOfPosts = listOfPosts.map {
            if (it.id != post.id) it else it.copy(text = post.text)
        }
        liveData.value = listOfPosts
        sync()
    }

    private fun update(post: Post) {
        listOfPosts = listOfPosts.map {
            if (it.id != post.id) it else post
        }.toMutableList()
        liveData.value = listOfPosts
        sync()
    }

    private fun sync() {
        context.openFileOutput(fileName,Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(listOfPosts))
        }
    }
}