package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.data.dao.PostDao
import ru.netology.nmedia.data.entity.PostEntity
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override fun get(): LiveData<List<Post>> = dao.getAll().map { posts ->
        posts.map {
            it.toDto()
        }
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            // Если id == 0, значит это новый пост, вставляем его
            dao.save(PostEntity.fromDto(post))
        } else {
            // Иначе обновляем существующий пост
            dao.update(PostEntity.fromDto(post))
        }
    }

    override fun likePost(id: Long) {
        dao.likeById(id)
    }

    override fun sharePost(id: Long) {
        dao.sharePost(id)
    }

    override fun plusView(id: Long) {
        dao.plusView(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

}
