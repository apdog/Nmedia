package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.dao.PostDao
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.domain.PostRepository
import java.util.Date

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    private var newDate = Date(System.currentTimeMillis() - (360 * 60 * 1000))
    private var listOfPosts = emptyList<Post>()

    private val liveData = MutableLiveData(listOfPosts)

    override fun get(): LiveData<List<Post>> = liveData

    init {
        listOfPosts = dao.getAll()
        liveData.value = listOfPosts
    }

    override fun likePost(id: Int) {
        dao.likeById(id)
        listOfPosts = listOfPosts.map { post ->
            if (post.id != id) post else post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
            )
        }
        liveData.value = listOfPosts
    }

    override fun sharePost(id: Int) {
        dao.sharePost(id)
        listOfPosts = listOfPosts.map { post ->
            if (post.id != id) post else post.copy(
                reposts = post.reposts + 1,
                views = post.views + 1
            )
        }
        liveData.value = listOfPosts
    }

    override fun plusView(id: Int) {
        dao.plusView(id)
        listOfPosts = listOfPosts.map { post ->
            if (post.id != id) post else post.copy(
                views = post.views + 1
            )
        }
        liveData.value = listOfPosts
    }


    override fun removeById(id: Int) {
        dao.removeById(id)
        listOfPosts = listOfPosts.filter { it.id != id }
        liveData.value = listOfPosts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        listOfPosts = if (id == 0) {
            listOf(saved) + listOfPosts
        } else {
            listOfPosts.map {
                if (it.id != id) it else saved
            }
        }
        update(post)
        liveData.value = listOfPosts
    }

    private fun update(post: Post) {
        listOfPosts = listOfPosts.map {
            if (it.id != post.id) it else post
        }.toMutableList()
        liveData.value = listOfPosts
    }
}