package ru.netology.nmedia.presentation.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.domain.post.Post
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

        viewModel.data.observe(this) { post ->
            updateUI(post)

            with(binding) {

                postLikesImageView.setOnClickListener {
                    viewModel.likePost()
                }

                postShareImageView.setOnClickListener{
                    viewModel.sharePost()
                    viewModel.plusView()
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
            postLikesCountTextView.text = formatCount(post.likes?.count ?: 0)
            postShareTextView.text = formatCount(post.reposts?.count ?: 0)
            postViewsTextView.text = formatCount(post.views?.count ?: 0)
            postCommentsCountTextView.text = (post.comments?.size ?: 0).toString()
            // Присвоение текстовой информации посту
            postTitleTextView.text = post.title
            postContentTextView.text = post.text
            postDateTextView.text =
                formatPostDate(post, applicationContext)
            postCommentsCountTextView.text = post.comments?.size.toString()
            // Обновление иконки лайка в зависимости от состояния likedByMe
            postLikesImageView.setImageResource(
                if (post.likedByMe) R.drawable.ic_filled_like_24
                else R.drawable.ic_like_border_24
            )
        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10_000 -> "${(count / 1000.0 * 10).toInt() / 10.0}K"
            count < 1_000_000 -> "${(count / 1000)}K"
            else -> "${(count / 1_000_000.0 * 10).toInt() / 10.0}M"
        }
    }

    private fun formatPostDate(post: Post, context: Context): String {
        val now = Date()
        val seconds = (now.time - post.date.time) / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = weeks / 4
        val years = months / 12
        val resources = context.resources ?: return ContextCompat.getString(
            context,
            R.string.unavailable
        )

        return when {
            years > 0 -> formatNumberWithSuffix(
                years, resources.getString(R.string.one_year),
                resources.getString(R.string.few_years), resources.getString(R.string.many_years)
            )

            months > 0 -> formatNumberWithSuffix(
                months, resources.getString(R.string.one_month),
                resources.getString(R.string.few_months), resources.getString(R.string.many_months)
            )

            weeks > 0 -> formatNumberWithSuffix(
                weeks, resources.getString(R.string.one_week),
                resources.getString(R.string.few_weeks), resources.getString(R.string.many_weeks)
            )

            days > 0 -> formatNumberWithSuffix(
                days, resources.getString(R.string.one_day),
                resources.getString(R.string.few_days), resources.getString(R.string.many_days)
            )

            hours > 0 -> formatNumberWithSuffix(
                hours, resources.getString(R.string.one_hour),
                resources.getString(R.string.few_hours), resources.getString(R.string.many_hours)
            )

            minutes > 0 -> formatNumberWithSuffix(
                minutes,
                resources.getString(R.string.one_minute),
                resources.getString(R.string.few_minutes),
                resources.getString(R.string.many_minutes)
            )

            else -> resources.getString(R.string.just_now)
        }
    }

    private fun formatNumberWithSuffix(
        number: Long,
        one: String,
        few: String,
        many: String
    ): String {
        return when {
            number % 10 == 1L && number % 100 != 11L -> "$number $one"
            number % 10 in 2..4 && number % 100 !in 12..14 -> "$number $few"
            else -> "$number $many"
        }
    }
}