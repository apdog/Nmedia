package ru.netology.nmedia.presentation.view

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.presentation.rv.OnInteractionListener
import ru.netology.nmedia.presentation.rv.PostListAdapter
import ru.netology.nmedia.presentation.viewModel.MainActivityViewModel
import ru.netology.nmedia.utils.AndroidUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postListAdapter: PostListAdapter

    //инициализация viewModel
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инициализация binding для привязки представления
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPostRecyclerView()

        viewModel.data.observe(this) { posts ->
            postListAdapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0) {
                return@observe
            }
            with(binding.contentField) {
                requestFocus()
                setText(post.text)
            }
        }
    }

    private fun initPostRecyclerView() {

        postListAdapter = PostListAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                binding.editTextViewTitle.text = post.text
                binding.additionalField.visibility = VISIBLE
                viewModel.edit(post)
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
        },
            applicationContext
        )

        binding.postListRecyclerView.apply {
            adapter = postListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.saveButton.setOnClickListener {
            with(binding.contentField) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Content can't be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changePostContent(text.toString())
                viewModel.save()

                clearField()
            }
        }

        binding.cancelButton.setOnClickListener {
            clearField()
        }
    }

    private fun clearField() {
        with(binding.contentField) {
            binding.additionalField.visibility = GONE
            setText("")
            clearFocus()
            AndroidUtils.hideKeyboard(this)
        }
        viewModel.cancelEditing()
    }
}
