package ru.netology.nmedia.repository

import android.widget.ImageView
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getAllAsync(callback: PostCallback<List<Post>>)
    fun likeById(id: Long, callback: PostCallback<Unit>)
    fun save(post: Post, callback: PostCallback<Unit>)
    fun removeById(id: Long, callback: PostCallback<Unit>)
    fun getPostById(id: Long, callback: PostCallback<Post>)
    fun setAvatar(imageView: ImageView, post: Post)
    fun setImageAttachment(imageView: ImageView, post: Post)

    interface PostCallback<T> {
        fun onSuccess(data: T) {}
        fun onError(e: Exception) {}
    }
}
