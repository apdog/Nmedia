package ru.netology.nmedia.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.presentation.rv.PostListAdapter
import ru.netology.nmedia.presentation.viewModel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var postlistRV: RecyclerView

    //инициализация viewModel
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инициализация binding для привязки представления
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPostRecyclerView()

        viewModel.data.observe(this) { posts ->
            postListAdapter.updateData(posts)
        }
    }

    private fun initPostRecyclerView() {
        postListAdapter = PostListAdapter(onLikeClicked = { post ->
            viewModel.likePost(post.id)
        }, onShareClicked = { post ->
            viewModel.sharePost(post.id)
            viewModel.plusView(post.id)
        })
        binding.postListRecyclerView.apply {
            adapter = postListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}
