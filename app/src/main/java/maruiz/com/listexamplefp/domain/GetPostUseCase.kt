package maruiz.com.listexamplefp.domain

import arrow.data.Reader
import arrow.data.ReaderApi
import arrow.data.flatMap
import arrow.data.map
import arrow.effects.IO
import maruiz.com.listexamplefp.data.datasource.PostSource
import maruiz.com.listexamplefp.data.model.PostModel
import maruiz.com.listexamplefp.di.context.PostContext

object GetPostUseCase {
    operator fun invoke(): Reader<PostContext, IO<List<PostModel>>> =
            ReaderApi.ask<PostContext>().flatMap { ctx ->
                PostSource.fetchPosts().map { postList ->
                    ctx.backgroundRunner(postList)
                }
            }
}