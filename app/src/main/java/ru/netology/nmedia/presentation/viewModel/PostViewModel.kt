package ru.netology.nmedia.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepositorySQLiteImpl
import ru.netology.nmedia.data.db.AppDb
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post
import java.util.Date

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // дата объект для сохранения
    private val emptyPost = Post(
        id = 0,
        fromId = 0,
        date = Date(),
        title = "",
        text = null,
        friendsOnly = false,
        comments = 0,
        likes = 0,
        likedByMe = false,
        reposts = 0,
        views = 0,
        isPinned = false,
        attachments = listOf()
    )

    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )
    val data: LiveData<List<Post>> = repository.get()

    private val edited = MutableLiveData(emptyPost)

    fun likePost(id: Int) {
        repository.likePost(id)
    }

    fun sharePost(id: Int) {
        repository.sharePost(id)
    }

    fun plusView(id: Int) {
        repository.plusView(id)
    }

    fun removeById(id: Int) {
        repository.removeById(id)
    }

    // фукция для сохранения
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = emptyPost
    }

    fun edit(post: Post) {
        edited.value = post
    }

    //функция изменения контента
    fun changePostContent(content: String) {
        val text = content.trim()
        if (edited.value?.text == text) {
            return
        }
        edited.value = edited.value?.copy(text = text)
    }

    fun cancelEditing() {
        edited.value = emptyPost
    }
}