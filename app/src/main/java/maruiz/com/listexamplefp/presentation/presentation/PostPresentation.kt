package maruiz.com.listexamplefp.presentation.presentation

import arrow.data.ReaderApi
import arrow.data.flatMap
import arrow.data.map
import maruiz.com.listexamplefp.di.context.PostContext
import maruiz.com.listexamplefp.domain.getPostsUseCase
import maruiz.com.listexamplefp.presentation.view.viewmodel.PostViewModel

object PostPresentation {

    fun getPosts() =
            ReaderApi.ask<PostContext>().flatMap { ctx ->
                getPostsUseCase().map {
                    it.unsafeRunAsync {
                        it.fold({ error -> ctx.view.showError(error.message ?: "") },
                                { success -> ctx.view.drawPost(success.map { PostViewModel(it.title, it.body) }) })
                    }
                }
            }
}