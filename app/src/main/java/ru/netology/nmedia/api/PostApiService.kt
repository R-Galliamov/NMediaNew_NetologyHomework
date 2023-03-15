package ru.netology.nmedia.api

import retrofit2.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

interface PostApiService {
    @GET("posts")
    fun getAll(): Call<List<Post>>

    @GET("posts/{id}")
    fun getPostByID(@Path("id") id: Long): Call<Post>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Unit>

    @DELETE("posts/{id}/likes")
    fun dislikeById(@Path("id") id: Long): Call<Unit>

    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Long): Call<Unit>

    @POST("posts")
    fun save(@Body post: Post): Call<Unit>
}

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val client = OkHttpClient.Builder().addInterceptor(logging).build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
        .baseUrl(BASE_URL).build()

object ApiPosts {
    val retrofitService: PostApiService by lazy {
        retrofit.create()
    }
}
