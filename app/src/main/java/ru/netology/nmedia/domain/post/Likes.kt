package ru.netology.nmedia.domain.post

data class Likes(
    val count: Int = 0,
    val userLikes: Int,
    val canLike: Boolean
)