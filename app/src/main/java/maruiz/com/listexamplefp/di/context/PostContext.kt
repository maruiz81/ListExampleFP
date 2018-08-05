package maruiz.com.listexamplefp.di.context

import arrow.effects.IO
import arrow.effects.async
import maruiz.com.listexamplefp.data.service.PostService
import maruiz.com.listexamplefp.presentation.view.activity.PostView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PostContext(val view: PostView) {
    private val baseUrl = "https://jsonplaceholder.typicode.com/"

    private val client: OkHttpClient

    init {
        val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

    val threading = IO.async()

    val postService: PostService = retrofit.create(PostService::class.java)
}