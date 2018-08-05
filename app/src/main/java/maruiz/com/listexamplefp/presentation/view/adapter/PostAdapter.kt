package maruiz.com.listexamplefp.presentation.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.post_row.view.*
import maruiz.com.listexamplefp.R
import maruiz.com.listexamplefp.presentation.view.viewmodel.PostViewModel

class PostAdapter(var posts: List<PostViewModel> = emptyList()) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false))

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: PostViewModel) {
            with(post) {
                itemView.title.text = post.title
                itemView.body.text = post.body
            }
        }
    }
}