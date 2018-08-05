package maruiz.com.listexamplefp.presentation.view.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import maruiz.com.listexamplefp.R
import maruiz.com.listexamplefp.di.context.PostContext
import maruiz.com.listexamplefp.presentation.presentation.PostPresentation
import maruiz.com.listexamplefp.presentation.view.adapter.PostAdapter
import maruiz.com.listexamplefp.presentation.view.viewmodel.PostViewModel

class PostActivity : AppCompatActivity(), PostView {
    private lateinit var adapter: PostAdapter

    private lateinit var postContext: PostContext
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        setUpDependencyGraph()
        setupRecylerView()
    }

    private fun setUpDependencyGraph() {
        postContext = PostContext(this)
    }

    private fun setupRecylerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter()
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        PostPresentation.getPosts().run(postContext)
    }

    override fun drawPost(posts: List<PostViewModel>) = runOnUiThread {
        adapter.posts = posts
        adapter.notifyDataSetChanged()
    }

    override fun showError(text: String) {
        Snackbar.make(recyclerView, text, Snackbar.LENGTH_SHORT).show()
    }
}
