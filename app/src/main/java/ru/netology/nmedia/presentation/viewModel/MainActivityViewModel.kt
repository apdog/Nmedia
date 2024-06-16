package ru.netology.nmedia.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.PostRepositoryImpl
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post
import java.util.Date

class MainActivityViewModel : ViewModel() {
    // дата объект для сохранения
    private val emptyPost = Post(
        id = 0,
        fromId = 0,
        date = Date(),
        title = "",
        text = null,
        friendsOnly = false,
        comments = null,
        likes = null,
        likedByMe = false,
        reposts = null,
        views = null,
        isPinned = false,
        attachments = listOf()
    )

    private val repository: PostRepository = PostRepositoryImpl
    val data: LiveData<List<Post>> = repository.get()

    val edited = MutableLiveData(emptyPost)

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

}