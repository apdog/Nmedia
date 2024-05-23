package ru.netology.nmedia.domain.post.attachments

import java.util.*

data class AudioAttachment(val audio: Audio) : Attachment("audio")

data class Audio(
    val id: Int, val ownerId: Int, // идентификатор владельца аудиозаписи
    val title: String?, // Название композиции
    val artist: String?, // исполнитель
    val duration: Int, // Длительность аудиозаписи в секундах
    val date: Date // Дата добавления
)