package maruiz.com.listexamplefp.di.context

import maruiz.com.listexamplefp.data.runner.BackgroundRunner
import maruiz.com.listexamplefp.data.service.PostService
import maruiz.com.listexamplefp.presentation.view.activity.PostView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

open class PostContext(val view: PostView,
                       val backgroundRunner: BackgroundRunner = BackgroundRunner(),
                       val apiclient: PostService = ApiImpl.retrofit.create(PostService::class.java)) {

    private object ApiImpl {
        private val baseUrl = "https://jsonplaceholder.typicode.com/"
        private val client: OkHttpClient

        init {
            val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

        val retrofit: Retrofit
            get() = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
    }
}