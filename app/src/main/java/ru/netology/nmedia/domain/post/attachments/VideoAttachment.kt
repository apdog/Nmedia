package ru.netology.nmedia.domain.post.attachments

import java.util.*

data class VideoAttachment(val video: Video) : Attachment("video")

data class Video(
    val id: Int,
    val ownerId: Int, // владелец видеозаписи
    val title: String?, //название видеозаписи
    val description: String?, // описание видеозаписи
    val duration: Int, // длительность видеозаписи
    val image: String?, // обложка
    val views: Int, // количество просмотров
    val date: Date // дата добавления
)