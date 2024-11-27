package ru.netology.nmedia.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post
import java.io.IOException
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

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body
                    if (body == null) {
                        callback.onError(RuntimeException("Body is null"))
                        return
                    }

                    try {
                        callback.onSuccess(gson.fromJson<List<Post>>(body.string(), typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    override fun likeById(id: Long, callback: PostRepository.PostCallback<Post>) {
        getAllAsync(object : PostRepository.PostCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                val post = posts.find { it.id == id } ?: return callback.onError(IllegalArgumentException("Пост не найден"))

                val request = Request.Builder()
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .apply { if (post.likedByMe) delete() else post("".toRequestBody()) }
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException("Ошибка сервера"))
                            return
                        }
                        response.body?.string()?.let {
                            callback.onSuccess(gson.fromJson(it, Post::class.java))
                        } ?: callback.onError(RuntimeException("Body is null"))
                    }
                })
            }

            override fun onError(error: Throwable) {
                callback.onError(error)
            }
        })
    }



    override fun removeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Ошибка сервера: ${response.code}"))
                        return
                    }
                    callback.onSuccess(Unit)
                }
            })
    }

    override fun save(post: Post, callback: PostRepository.PostCallback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Ошибка сервера: ${response.code}"))
                        return
                    }
                    try {
                        val savedPost = gson.fromJson(response.body?.string(), Post::class.java)
                        callback.onSuccess(savedPost)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

//    override fun sharePost(id: Long) {
//        TODO()
//    }
//
//    override fun plusView(id: Long) {
//        TODO()
//    }

    private fun updatePostInList(updatedPost: Post, posts: List<Post>) {
        val updatedPosts = posts.toMutableList()
        val index = updatedPosts.indexOfFirst { it.id == updatedPost.id }
        if (index != -1) {
            updatedPosts[index] = updatedPost
        }
    }

}
