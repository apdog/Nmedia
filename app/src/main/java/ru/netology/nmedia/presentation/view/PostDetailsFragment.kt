package ru.netology.nmedia.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.presentation.rv.OnInteractionListener
import ru.netology.nmedia.presentation.rv.PostListViewHolder
import ru.netology.nmedia.presentation.view.NewPostFragment.Companion.textArg
import ru.netology.nmedia.presentation.viewModel.PostViewModel

class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding
    private lateinit var viewHolder: PostListViewHolder
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        viewHolder = PostListViewHolder(binding.postDetails, object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likePost(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.sharePost(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_postDetailsFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.text
                        putBoolean("IS_EDIT_MODE", true)
                    })
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onVideoClick(videoUrl: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }, requireContext())

        val postId = arguments?.getInt("POST_ID") ?: return binding.root

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            viewHolder.bind(post, showFullText = true)
        }

        return binding.root
    }
}


