package ru.netology.nmedia.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.data.PostRepositoryImpl
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.presentation.viewModel.MainActivityViewModel
import ru.netology.nmedia.utils.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PostItemBinding

    //инициализация viewModel
    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(PostRepositoryImpl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инициализация binding для привязки представления
        binding = PostItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.liveData.observe(this) { post ->
            updateUI(post)

            with(binding) {
                postLikesImageView.setOnClickListener {
                    viewModel.likePost(post)
                }

                // Обработка клика на иконку репоста
                postShareImageView.setOnClickListener {
                    viewModel.sharePost(post)
                }

                // Обработка клика на кнопку "Еще"
                moreButton.setOnClickListener {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.open_additional_menu),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun updateUI(post: Post) {
        with(binding) {
            // Обновление счетчиков лайков, репостов, просмотров и комментариев
            postLikesCountTextView.text = PostRepositoryImpl.formatCount(post.likes?.count ?: 0)
            postShareTextView.text = PostRepositoryImpl.formatCount(post.reposts?.count ?: 0)
            postViewsTextView.text = PostRepositoryImpl.formatCount(post.views?.count ?: 0)
            postCommentsCountTextView.text = post.comments?.size.toString()
            // Присвоение текстовой информации посту
            postTitleTextView.text = post.title
            postContentTextView.text = post.text
            postDateTextView.text =
                PostRepositoryImpl.formatPostDate(post.date, applicationContext)
            postCommentsCountTextView.text = post.comments?.size.toString()
            // Обновление иконки лайка в зависимости от состояния likedByMe
            postLikesImageView.setImageResource(
                if (post.likedByMe) R.drawable.ic_filled_like_24
                else R.drawable.ic_like_border_24
            )
        }
    }
}
