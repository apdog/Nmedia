package ru.netology.nmedia.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.PostRepositoryImpl
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post

class MainActivityViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryImpl

    val data: LiveData<Post> = repository.get()

    fun likePost() {
        repository.likePost()
    }

    fun sharePost() {
        repository.sharePost()
    }

    fun plusView() {
        repository.plusView()
    }

    fun formatCount(count: Int): String {
        return repository.formatCount(count)
    }

    fun formatPostDate(post: Post, context: Context): String {
        return repository.formatPostDate(post, context)
    }
}