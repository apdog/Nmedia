package ru.netology.nmedia.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.domain.post.Post
import ru.netology.nmedia.data.PostRepositoryImpl
import ru.netology.nmedia.presentation.viewModel.MainActivityViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PostItemBinding

    //инициализация viewModel
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инициализация binding для привязки представления
        binding = PostItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //// Создание новой даты для поста
        val newDate = Date(System.currentTimeMillis() - (360 * 60 * 1000))

        // Добавление нового поста через ViewModel
        with(binding) {

            viewModel.addPost(
                Post(
                    id = 0,
                    fromId = 0,
                    date = newDate, // Используем переменную newDate
                    title = getString(R.string.exampleTextTitleOfPost),
                    text = getString(R.string.exampleTextOfPost),
                    friendsOnly = false,
                    comments = null,
                    likes = null,
                    likedByMe = false,
                    isPinned = false,
                    reposts = null,
                    views = null,
                    attachments = null
                )
            )

            // Наблюдение за изменениями поста с id = 1 и обновление UI
            viewModel.getPostById(1).observe(this@MainActivity) { post ->
                updateUI(post)
            }

            // Обработка клика на иконку лайка
            postLikesImageView.setOnClickListener() {
                // Получение текущего поста из LiveData
                viewModel.getPostById(1).value?.let { post ->
                    // Лайк поста через ViewModel и обновление UI
                    viewModel.likePost(post).observe(this@MainActivity) { updatedPost ->
                        updateUI(updatedPost)
                    }
                }
            }

            // Обработка клика на иконку репоста
            postShareImageView.setOnClickListener {
                // Получение текущего поста из LiveData
                viewModel.getPostById(1).value?.let { post ->
                    // Репост поста через ViewModel
                    viewModel.sharePost(post).observe(this@MainActivity) {updatedPost ->
                        updateUI(updatedPost)
                    }
                }
            }

            // Обработка клика на кнопку "Еще"
            moreButton.setOnClickListener {
                Toast.makeText(
                    applicationContext, getString(R.string.open_additional_menu), Toast.LENGTH_SHORT
                ).show()
            }


        }
    }

    // Метод для обновления UI на основе данных поста
    private fun updateUI(post: Post?) {
        // Проверка на null, чтобы избежать NullPointerException
        post?.let {
            with(binding) {
                // Обновление счетчиков лайков, репостов, просмотров и комментариев
                postLikesCountTextView.text = PostRepositoryImpl.formatCount(post.likes?.count ?: 0)
                postShareTextView.text = PostRepositoryImpl.formatCount(post.reposts?.count ?: 0)
                postViewsTextView.text = PostRepositoryImpl.formatCount(post.views?.count ?: 0)
                postCommentsCountTextView.text = post.comments?.size.toString()
                // Присвоение текстовой информации посту
                postTitleTextView.text = post.title
                postContentTextView.text = post.text
                postDateTextView.text = PostRepositoryImpl.formatPostDate(post.date, applicationContext)
                postCommentsCountTextView.text = post.comments?.size.toString()
                // Обновление иконки лайка в зависимости от состояния likedByMe
                postLikesImageView.setImageResource(
                    if (post.likedByMe) R.drawable.ic_filled_like_24
                    else R.drawable.ic_like_border_24
                )
            }
        }
    }

}