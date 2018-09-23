package maruiz.com.listexamplefp.data.datasource

import arrow.data.Reader
import arrow.data.ReaderApi
import arrow.data.map
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.monadDefer
import arrow.typeclasses.bindingCatch
import maruiz.com.listexamplefp.data.model.PostModel
import maruiz.com.listexamplefp.data.util.tryRunAndFoldResultORError
import maruiz.com.listexamplefp.di.context.PostContext

object PostSource {
    fun fetchPosts(): Reader<PostContext, IO<List<PostModel>>> =
            ReaderApi.ask<PostContext>().map { ctx ->
                IO.monadDefer().bindingCatch {
                    tryRunAndFoldResultORError(
                            f = { ctx.apiclient.getPosts().execute().body() ?: throw Exception("Error") },
                            onError = { IO.raiseError<List<PostModel>>(it) },
                            onSuccess = { IO.just(it) }).bind().bind()
                }.fix()
            }
}