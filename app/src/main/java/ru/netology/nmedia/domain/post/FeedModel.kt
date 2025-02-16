package ru.netology.nmedia.domain.post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val error: Boolean = false,
    val loading: Boolean = false,
    val empty: Boolean = false,
    val refreshing: Boolean = false,
    val errorMessage: String? = null,
)

