package ru.netology.nmedia

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.post.WallService
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PostItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PostItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newDate = Date(System.currentTimeMillis() - (360 * 60 * 1000))

        WallService.add(
            Post(
                id = 0,
                fromId = 0,
                date = newDate,
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

        with(binding) {

            val post = WallService.getPostById(1)
            //обновления счетчиков при первоначальной загрузке приложения
            postLikesCountTextView.text = WallService.formatCount(post?.likes?.count ?: 0)
            postShareTextView.text = WallService.formatCount(post?.reposts?.count ?: 0)
            postViewsTextView.text = WallService.formatCount(post?.views?.count ?: 0)
            postCommentsCountTextView.text = post?.comments?.size.toString()
            //присвоение текстовой инфы посту
            postTitleTextView.text = post?.title
            postContentTextView.text = post?.text
            postDateTextView.text = WallService.formatPostDate(post?.date, applicationContext)
            postCommentsCountTextView.text = post?.comments?.size.toString()

            moreButton.setOnClickListener {
                Toast.makeText(
                    applicationContext, getString(R.string.open_additional_menu), Toast.LENGTH_SHORT
                ).show()
            }

            if (post!!.likedByMe) {
                postLikesImageView.setImageResource(R.drawable.ic_like_border_24)
            }

            postLikesImageView.setOnClickListener {
                WallService.likePost(post)
                postLikesCountTextView.text = post.likes?.count?.let { WallService.formatCount(it)}
                postLikesImageView.setImageResource(
                    if (post.likedByMe) R.drawable.ic_filled_like_24
                    else R.drawable.ic_like_border_24
                )
            }

            postShareImageView.setOnClickListener {
                WallService.sharePost(post)
                postShareTextView.text =
                    post.reposts?.count?.let { WallService.formatCount(it) }
                // для просмотров
                WallService.plusView(post)
                postViewsTextView.text =
                    post.views?.count?.let { WallService.formatCount(it) }
            }
        }
    }
}