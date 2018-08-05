package maruiz.com.listexamplefp.data.datasource

import arrow.Kind
import arrow.core.Try
import arrow.core.right
import arrow.data.Reader
import arrow.data.ReaderApi
import arrow.data.map
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.monadDefer
import arrow.effects.typeclasses.Async
import arrow.typeclasses.bindingCatch
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import maruiz.com.listexamplefp.data.model.PostModel
import maruiz.com.listexamplefp.di.context.PostContext

object PostSource {
    private fun <F, A, B> runInAsyncContext(f: () -> A, onError: (Throwable) -> B,
                                            onSuccess: (A) -> B, AC: Async<F>): Kind<F, B> =
            AC.async { callback ->
                async(CommonPool) {
                    val result = Try { f() }.fold(onError, onSuccess)
                    callback(result.right())
                }
            }

    fun fetchPosts(): Reader<PostContext, IO<List<PostModel>>> =
            ReaderApi.ask<PostContext>().map { ctx ->
                IO.monadDefer().bindingCatch {
                    val result = runInAsyncContext(
                            f = { ctx.postService.getPosts().execute().body() ?: throw Exception() },
                            onError = { IO.raiseError<List<PostModel>>(it) },
                            onSuccess = { IO.just(it) }, AC = ctx.threading).bind()
                    result.bind()
                }.fix()
            }
}