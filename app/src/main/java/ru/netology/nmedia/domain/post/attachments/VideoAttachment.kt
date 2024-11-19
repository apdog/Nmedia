package ru.netology.nmedia.domain.post.attachments

data class VideoAttachment(val video: Video) : Attachment("video")

data class Video(
    val id: Long,
    val ownerId: Int, // владелец видеозаписи
    val title: String?, //название видеозаписи
    val description: String?, // описание видеозаписи
    val duration: Int, // длительность видеозаписи
    val image: Int, // обложка
    val url: String
)