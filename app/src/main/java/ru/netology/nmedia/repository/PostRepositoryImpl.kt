package ru.netology.nmedia.repository

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://192.168.0.102:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun setAvatar(avatarView: ImageView, post: Post) {
        val authorAvatar = post.authorAvatar
        val url = "$BASE_URL/avatars/$authorAvatar"
        Glide.with(avatarView)
            .load(url)
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .error(R.drawable.ic_baseline_account_circle_24)
            .apply(RequestOptions().circleCrop())
            .into(avatarView)
    }

    override fun setImageAttachment(imageView: ImageView, post: Post) {
        if (post.attachment != null) {
            val imageName = post.attachment.url
            val url = "$BASE_URL/images/$imageName"
            Glide.with(imageView)
                .load(url)
                .placeholder(R.drawable.ic_baseline_downloading_24)
                .error(R.drawable.ic_baseline_do_not_disturb_24)
                .into(imageView)
        }
    }

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun getPostById(id: Long, callback: PostRepository.PostCallback<Post>) {
        val request: Request = Request.Builder()
            .url(("${BASE_URL}/api/slow/posts/$id"))
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        getPostById(id, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(post: Post) {
                val request: Request = if (post.likedByMe) {
                    Request.Builder()
                        .url("${BASE_URL}/api/posts/$id/likes")
                        .delete("".toRequestBody(jsonType))
                        .build()
                } else {
                    Request.Builder()
                        .url("${BASE_URL}/api/posts/$id/likes")
                        .post("".toRequestBody(jsonType))
                        .build()
                }

                client.newCall(request)
                    .enqueue(object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            try {
                                callback.onSuccess(Unit)
                            } catch (e: Exception) {
                                callback.onError(e)
                            }
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            callback.onError(e)
                        }
                    })
            }

            override fun onError(e: Exception) {
                callback.onError(e)
            }
        })
    }

    override fun save(post: Post, callback: PostRepository.PostCallback<Unit>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
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
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
}
