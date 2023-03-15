package ru.netology.nmedia.repository

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import ru.netology.nmedia.R
import ru.netology.nmedia.api.ApiPosts
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostRepositoryImpl : PostRepository {

    companion object {
        private const val BASE_URL = "http://192.168.0.102:9999"
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
