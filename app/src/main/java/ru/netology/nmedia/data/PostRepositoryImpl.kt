package ru.netology.nmedia.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private val typeToken = object : TypeToken<List<Post>>() {}

    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999"
        val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("Body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun save(post: Post){
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun likeById(id: Long) {
        val post = getAll().find { it.id == id } ?: throw IllegalArgumentException("Пост не найден")
        val request = if (post.likedByMe) {
            Request.Builder()
                .delete()
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()
        } else {
            Request.Builder()
                .post("".toRequestBody())
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()
        }

        client.newCall(request).execute().use { response ->
            val updatedPost = gson.fromJson(response.body?.string(), Post::class.java)
            updatePostInList(updatedPost)
        }
    }

    override fun sharePost(id: Long) {
        TODO()
    }

    override fun plusView(id: Long) {
        TODO()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    private fun updatePostInList(updatedPost: Post) {
        val posts = getAll().toMutableList()
        val index = posts.indexOfFirst { it.id == updatedPost.id }
        if (index != -1) {
            posts[index] = updatedPost
        }
    }

}
