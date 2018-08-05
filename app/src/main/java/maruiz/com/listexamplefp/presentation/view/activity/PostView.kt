package maruiz.com.listexamplefp.presentation.view.activity

import maruiz.com.listexamplefp.presentation.view.viewmodel.PostViewModel

interface PostView {
    fun drawPost(posts: List<PostViewModel>)
    fun showError(text: String)
}