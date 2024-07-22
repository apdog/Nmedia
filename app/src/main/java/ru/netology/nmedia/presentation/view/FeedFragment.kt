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
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.presentation.rv.OnInteractionListener
import ru.netology.nmedia.presentation.rv.PostListAdapter
import ru.netology.nmedia.presentation.view.NewPostFragment.Companion.textArg
import ru.netology.nmedia.presentation.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private lateinit var postListAdapter: PostListAdapter
    private var isEditing = false

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        initPostRecyclerView()

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            postListAdapter.submitList(posts)
            //прокрутка при добавлении поста
            if (!isEditing) {
                binding.postListRecyclerView.scrollToPosition(0)
            }

        }

        binding.addPostFAB.setOnClickListener {
            isEditing = false
            viewModel.cancelEditing()
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }

    private fun initPostRecyclerView() {
        postListAdapter = PostListAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                isEditing = true
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.text
                        putBoolean("IS_EDIT_MODE", true)
                    }
                )
            }

            override fun onLike(post: Post) {
                viewModel.likePost(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.sharePost(post.id)
                viewModel.plusView(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onVideoClick(videoUrl: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            override fun onPostClick(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postDetailsFragment,
                    Bundle().apply {
                        putInt("POST_ID", post.id)
                    }
                )
            }

        },
            requireContext()
        )

        binding.postListRecyclerView.apply {
            adapter = postListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
