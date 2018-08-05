package maruiz.com.listexamplefp.domain

import arrow.data.Reader
import arrow.effects.IO
import maruiz.com.listexamplefp.data.datasource.PostSource
import maruiz.com.listexamplefp.data.model.PostModel
import maruiz.com.listexamplefp.di.context.PostContext

fun getPostsUseCase(): Reader<PostContext, IO<List<PostModel>>> = PostSource.fetchPosts()