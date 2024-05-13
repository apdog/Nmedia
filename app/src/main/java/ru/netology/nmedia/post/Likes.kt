package ru.netology.nmedia.post

data class Likes(
    var count: Int = 0,
    val userLikes: Int,
    val canLike: Boolean
)