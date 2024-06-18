package ru.netology.nmedia.presentation.rv

import ru.netology.nmedia.domain.post.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
}