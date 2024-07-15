package ru.netology.nmedia.presentation.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.domain.post.Post

class PostListAdapter(
    private val listener: OnInteractionListener,
    private val context: Context
) : ListAdapter<Post, PostListViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListViewHolder {
        return PostListViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener,
            context
        )
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}