package maruiz.com.listexamplefp.presentation.view.viewodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import arrow.data.Reader
import arrow.data.ReaderApi
import arrow.data.flatMap
import arrow.data.map
import maruiz.com.listexamplefp.data.model.PostModel
import maruiz.com.listexamplefp.di.context.PostContext
import maruiz.com.listexamplefp.domain.getPostsUseCase
import maruiz.com.listexamplefp.presentation.view.model.PostPresentationModel

class PostViewModel : ViewModel() {

    var postList: MutableLiveData<List<PostPresentationModel>> = MutableLiveData()

    var error: MutableLiveData<String> = MutableLiveData()

    private fun handleSuccess(postList: List<PostModel>) {
        this.postList.value = postList.map { PostPresentationModel(it.title, it.body) }
    }

    private fun handleError(error: Throwable) {
        this.error.value = error.message
    }

    fun getPosts(): Reader<PostContext, Unit> =
            ReaderApi.ask<PostContext>().flatMap {
                getPostsUseCase().map {
                    it.unsafeRunAsync {
                        it.fold(::handleError, ::handleSuccess)
                    }
                }
            }
}