package ru.netology.nmedia.post.attachments

import java.util.*

data class UrlAttachment(val url: Url) : Attachment("url")

data class Url(
    val id: Int, val title: String, // Заголовок ссылки
    val caption: String, // подпись ссылки
    val description: String, // описание ссылки
    val date: Date, // дата добавления
    val size: Int // размер файла в байтах
)