package ru.netology.nmedia.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post

class MainActivityViewModel(private val repository: PostRepository) : ViewModel() {

    val liveData: LiveData<Post> = repository.get()

    fun addPost(post: Post): LiveData<Post> {
        return repository.add(post)
    }

    fun updatePost(post: Post): LiveData<Boolean> {
        return repository.update(post)
    }

//    fun createComment(postId: Int, comment: Comments): LiveData<Comments> {
//        return repository.createComment(postId, comment)
//    }
    fun likePost(post: Post): LiveData<Post> {
        return repository.likePost(post)
    }

    fun sharePost(post: Post): LiveData<Post> {
        return repository.sharePost(post)
    }

    fun plusView(post: Post): LiveData<Post> {
        return repository.plusView(post)
    }
}