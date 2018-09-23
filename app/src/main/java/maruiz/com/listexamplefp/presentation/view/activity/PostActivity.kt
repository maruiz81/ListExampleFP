package maruiz.com.listexamplefp.presentation.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import maruiz.com.listexamplefp.R
import maruiz.com.listexamplefp.di.context.PostContext
import maruiz.com.listexamplefp.presentation.view.adapter.PostAdapter
import maruiz.com.listexamplefp.presentation.view.model.PostPresentationModel
import maruiz.com.listexamplefp.presentation.view.viewodel.PostViewModel

class PostActivity : AppCompatActivity() {
    private lateinit var adapter: PostAdapter

    private lateinit var postContext: PostContext

    private val postViewModel by lazy { ViewModelProviders.of(this).get(PostViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        setUpDependencyGraph()
        setupRecylerView()

        postViewModel.postList.observe(this, Observer {
            it?.let { drawPost(it) }
        })

        postViewModel.error.observe(this, Observer {
            it?.let { showError(it) }
        })
    }

    override fun onResume() {
        super.onResume()
        postViewModel.getPosts().run(postContext)
    }

    private fun setUpDependencyGraph() {
        postContext = PostContext(this)
    }

    private fun setupRecylerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter()
        recyclerView.adapter = adapter
    }

    private fun drawPost(posts: List<PostPresentationModel>) = runOnUiThread {
        adapter.posts = posts
        adapter.notifyDataSetChanged()
    }

    private fun showError(text: String) {
       Toast.makeText(this, text, Snackbar.LENGTH_LONG).show()
    }
}
