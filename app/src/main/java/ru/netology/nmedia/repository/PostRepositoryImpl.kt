package ru.netology.nmedia.repository

import ru.netology.nmedia.api.ApiPosts
import ru.netology.nmedia.dto.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        ApiPosts.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                        callback.onError(Exception(response.code().toString()))
                } else {
                    callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t))
            }

        })
    }

    override fun getPostById(id: Long, callback: PostRepository.PostCallback<Post>) {
        ApiPosts.retrofitService.getPostByID(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception(response.code().toString()))
                } else {
                    callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun likeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        getPostById(id, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(post: Post) {
                if (post.likedByMe) {
                    ApiPosts.retrofitService.dislikeById(id)
                } else {
                    ApiPosts.retrofitService.likeById(id)
                }
            }

            override fun onError(e: Exception) {
                super.onError(e)
            }
        })
    }

    override fun save(post: Post, callback: PostRepository.PostCallback<Unit>) {
        ApiPosts.retrofitService.save(post).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception(response.code().toString()))
                } else {
                    callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun removeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        ApiPosts.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception(response.code().toString()))
                } else {
                    callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

}
