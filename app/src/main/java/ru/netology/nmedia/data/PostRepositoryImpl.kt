package ru.netology.nmedia.data


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.domain.PostRepository
import ru.netology.nmedia.domain.post.Post


class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                } else {
                    callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<List<Post>?>, e: Throwable) {
                callback.onError(Exception(e))
            }

        })

    }

    override fun likeById(id: Long, callback: PostRepository.PostCallback<Post>) {
        PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun unlikeById(id: Long, callback: PostRepository.PostCallback<Post>) {
        PostsApi.retrofitService.dislikeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun removeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun save(post: Post, callback: PostRepository.PostCallback<Post>) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError(RuntimeException("Response body is null"))
                } else {
                    callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
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


}
