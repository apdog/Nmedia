package ru.netology.nmedia.domain.post.attachments

import java.util.*

data class PhotoAttachment(val photo: Photo) : Attachment("photo")

data class Photo(
    val id: Int,
    val text: String, // текст описания
    val date: Date // дата добавления
)