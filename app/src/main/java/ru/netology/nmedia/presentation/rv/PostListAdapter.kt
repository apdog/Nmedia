package ru.netology.nmedia.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.domain.post.Post

class PostListAdapter(
    private val onLikeClicked: (Post) -> Unit,
    private val onShareClicked: (Post) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfPosts = listOf<Post>()

    override fun getItemCount() = listOfPosts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostListViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onLikeClicked,
            onShareClicked
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostListViewHolder -> {
                holder.bind(listOfPosts[position])
            }
        }
    }

    fun updateData(newPostsList: List<Post>) {
        val oldPostsList = listOfPosts
        val diff = PostDiff(oldPostsList, newPostsList)
        val diffResult = DiffUtil.calculateDiff(diff)
        listOfPosts = newPostsList
        diffResult.dispatchUpdatesTo(this)
    }

}