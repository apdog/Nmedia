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
    author = "Нетология",
    content = "",
    published = 0L,
    likedByMe = false,
    likes = 0,
    attachments = listOf(),
    title = "",
    authorAvatar = ""
//    friendsOnly = false,
//    comments = 0,
//    reposts = 0,
//    views = 0,
//    isPinned = false,

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
        repository.likeById(id, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(updatedPost: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty().map { post ->
                        if (post.id == updatedPost.id) updatedPost else post
                    })
                )
            }

            override fun onError(error: Throwable) {
                _data.postValue(_data.value?.copy(error = true))
            }
        })
    }


//    fun sharePost(id: Long) {
//        thread { repository.sharePost(id) }
//    }
//
//    fun plusView(id: Long) {
//        thread { repository.plusView(id) }
//    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty().filter { it.id != id })
        )
        repository.removeById(id, object : PostRepository.PostCallback<Unit> {
            override fun onSuccess(result: Unit) {
                // Успешное удаление — пост уже удалён из списка.
            }

            override fun onError(error: Throwable) {
                _data.postValue(_data.value?.copy(posts = old, error = true))
            }
        })
    }

    fun loadPosts() {

        _data.postValue(FeedModel(loading = true))

        repository.getAllAsync(
            object : PostRepository.PostCallback<List<Post>> {
                override fun onSuccess(result: List<Post>) {
                    _data.postValue(FeedModel(posts = result, empty = result.isEmpty()))
                }

                override fun onError(error: Throwable) {
                    _data.postValue(FeedModel(error = true))
                }

            }
        )
    }

    // фукция для сохранения
    fun save() {
        edited.value?.let {
            repository.save(it, object : PostRepository.PostCallback<Post> {
                override fun onSuccess(result: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(error: Throwable) {
                    _data.postValue(_data.value?.copy(error = true))
                }
            })
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