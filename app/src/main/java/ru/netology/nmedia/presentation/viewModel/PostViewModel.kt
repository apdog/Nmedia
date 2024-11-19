package ru.netology.nmedia.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepositoryImpl
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.FeedModel
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.utils.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

// дата объект для сохранения
private val empty = Post(
    id = 0L,
    author = 0L,
    date = 0L,
    title = "",
    content = null,
    friendsOnly = false,
    comments = 0,
    likes = 0,
    likedByMe = false,
    reposts = 0,
    views = 0,
    isPinned = false,
    attachments = listOf()
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel> = _data

    private val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun likePost(id: Long) {
        thread {
            try {
                repository.likeById(id)
                loadPosts()
            } catch (e: IOException) {
                TODO()
            }
        }
    }

    fun sharePost(id: Long) {
        thread { repository.sharePost(id) }
    }

    fun plusView(id: Long) {
        thread { repository.plusView(id) }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))

            try {
               val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: Exception) {
                FeedModel(error = true)
            }.let(_data::postValue)
        }
    }

    // фукция для сохранения
    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    //функция изменения контента
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun cancelEditing() {
        edited.value = empty
    }
}