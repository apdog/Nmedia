package ru.netology.nmedia.presentation.rv

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.domain.post.Post
import java.util.Date


class PostListViewHolder(
    private val binding: PostItemBinding,
    private val listener: OnInteractionListener,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post, showFullText: Boolean = false) {
        with(binding) {
            // Обновление счетчиков лайков, репостов, просмотров и комментариев
            postLikesButton.text = formatCount(post.likes)
//            postCommentsButton.text = formatCount(post.comments)
//            postShareButton.text = formatCount(post.reposts)
//            postViewsButton.text = formatCount(post.views)
//            postCommentsButton.text = post.comments.toString()
            // Присвоение текстовой информации посту
            postTitleTextView.text = post.author
            postContentTextView.text = post.content
            postDateTextView.text = formatPostDate(post)
            videoAndImageContainer.visibility = View.GONE
            // аватарка
            val avatarUrl = post.authorAvatar?.let { "http://10.0.2.2:9999/avatars/$it" }

            if (avatarUrl != null) {
                Glide.with(context)
                    .load(avatarUrl) // Загружаем изображение с сервера по URL
                    .timeout(5100)
                    .circleCrop()
                    .error(R.drawable.ic_cancel_24)
                    .placeholder(R.drawable.placeholder_img)
                    .into(avatarImageView)
            } else {
                avatarImageView.setImageResource(R.drawable.ic_cancel_24)
            }

            // Изображение
            val imageUrl = post.attachment?.url.let { "http://10.0.2.2:9999/images/$it" }

            if (post.attachment != null) {
                Glide.with(context)
                    .load(imageUrl) // Загружаем изображение с сервера по URL
                    .timeout(5100)
                    .error(R.drawable.ic_cancel_24)
                    .placeholder(R.drawable.placeholder_img)
                    .into(imageThumbnail)
                videoAndImageContainer.visibility = View.VISIBLE
            } else {
                avatarImageView.setImageResource(R.drawable.ic_cancel_24)
            }
//            postCommentsButton.text = post.comments.toString()
            // Обновление иконки лайка в зависимости от состояния likedByMe
            postLikesButton.isChecked = post.likedByMe

            if (showFullText) {
                postContentTextView.maxLines = Integer.MAX_VALUE
                postContentTextView.ellipsize = null
            } else {
                postContentTextView.maxLines = 10
                postContentTextView.ellipsize = TextUtils.TruncateAt.END
            }

            // Установка слушателей на кнопки лайка и репоста
            postLikesButton.setOnClickListener {
                listener.onLike(post)
            }

            // Установка слушателя на пост
            root.setOnClickListener {
                listener.onPostClick(post)
            }

            postShareButton.setOnClickListener {
                listener.onShare(post)
            }

            optionMenuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_option_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.removePost -> {
                                listener.onRemove(post)
                                true
                            }
                            R.id.editPost -> {
                                listener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }

//        // Если есть видео
//        val videoAttachment = post.attachments?.find { it is VideoAttachment } as? VideoAttachment
//        if (videoAttachment != null) with(binding) {
//            videoContainer.visibility = View.VISIBLE
//            videoTitle.text = videoAttachment.video.title
//            // Подгружаем изображение
//            videoThumbnail.setImageResource(videoAttachment.video.image)
//            val videoUrl = videoAttachment.video.url
//            videoContainer.setOnClickListener {
//                listener.onVideoClick(videoUrl)
//            }
//            playButton.setOnClickListener {
//                listener.onVideoClick(videoUrl)
//            }
//        } else {
//            binding.videoContainer.visibility = View.GONE
//        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10_000 -> "${(count / 1000.0 * 10).toInt() / 10.0}K"
            count < 1_000_000 -> "${(count / 1000)}K"
            else -> "${(count / 1_000_000.0 * 10).toInt() / 10.0}M"
        }
    }

    private fun formatPostDate(post: Post): String {
        val now = Date()
        val seconds = (now.time - post.published) / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = weeks / 4
        val years = months / 12
        val resources = context.resources ?: return ContextCompat.getString(
            context, R.string.unavailable
        )

        return when {
            years > 0 -> formatNumberWithSuffix(
                years,
                resources.getString(R.string.one_year),
                resources.getString(R.string.few_years),
                resources.getString(R.string.many_years)
            )

            months > 0 -> formatNumberWithSuffix(
                months,
                resources.getString(R.string.one_month),
                resources.getString(R.string.few_months),
                resources.getString(R.string.many_months)
            )

            weeks > 0 -> formatNumberWithSuffix(
                weeks,
                resources.getString(R.string.one_week),
                resources.getString(R.string.few_weeks),
                resources.getString(R.string.many_weeks)
            )

            days > 0 -> formatNumberWithSuffix(
                days,
                resources.getString(R.string.one_day),
                resources.getString(R.string.few_days),
                resources.getString(R.string.many_days)
            )

            hours > 0 -> formatNumberWithSuffix(
                hours,
                resources.getString(R.string.one_hour),
                resources.getString(R.string.few_hours),
                resources.getString(R.string.many_hours)
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
        number: Long, one: String, few: String, many: String
    ): String {
        return when {
            number % 10 == 1L && number % 100 != 11L -> "$number $one"
            number % 10 in 2..4 && number % 100 !in 12..14 -> "$number $few"
            else -> "$number $many"
        }
    }
}