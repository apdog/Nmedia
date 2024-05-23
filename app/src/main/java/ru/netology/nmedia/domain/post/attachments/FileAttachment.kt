package ru.netology.nmedia.domain.post.attachments

import java.util.*

data class FileAttachment(val file: File) : Attachment("file")

data class File(
    val id: Int,
    val title: String, // название файла
    val date: Date, // дата добавления
    val size: Int // размер файла в байтах
)