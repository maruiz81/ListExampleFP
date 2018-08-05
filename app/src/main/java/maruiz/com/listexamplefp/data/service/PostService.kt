package maruiz.com.listexamplefp.data.service

import maruiz.com.listexamplefp.data.model.PostModel
import retrofit2.Call
import retrofit2.http.GET

interface PostService {
    @GET("posts")
    fun getPosts(): Call<List<PostModel>>
}