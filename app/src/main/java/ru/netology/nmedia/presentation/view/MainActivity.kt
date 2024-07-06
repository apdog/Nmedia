package ru.netology.nmedia.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.presentation.rv.OnInteractionListener
import ru.netology.nmedia.presentation.rv.PostListAdapter
import ru.netology.nmedia.presentation.viewModel.MainActivityViewModel
import ru.netology.nmedia.utils.NewPostResultContract

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postListAdapter: PostListAdapter

    private val viewModel: MainActivityViewModel by viewModels()

    private val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
        result ?: return@registerForActivityResult
        viewModel.changePostContent(result)
        viewModel.save()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPostRecyclerView()

        viewModel.data.observe(this) { posts ->
            postListAdapter.submitList(posts)
        }

        binding.addPostFAB.setOnClickListener {
            viewModel.cancelEditing()
            newPostLauncher.launch(Pair(null, false)) // Новый пост
        }
    }

    private fun initPostRecyclerView() {
        postListAdapter = PostListAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                newPostLauncher.launch(Pair(post.text, true))
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
        },
            applicationContext
        )

        binding.postListRecyclerView.apply {
            adapter = postListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}
