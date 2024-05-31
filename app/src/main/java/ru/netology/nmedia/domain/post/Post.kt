package ru.netology.nmedia.domain.post

import ru.netology.nmedia.domain.post.attachments.Attachment
import java.util.Date

data class Post(
    val id: Int, // Уникальный номер поста
    val fromId: Int, // ID автора поста
    val date: Date, // Дата публикации поста
    val title: String, // заголовок поста
    val text: String?, // Текст поста
    val friendsOnly: Boolean, // если пост виден только друзьям
    val comments: MutableList<Comments>? = mutableListOf(), // Информация о комментариях
    val likes: Likes?, // Информация о лайках
    val likedByMe: Boolean = false, // Лайкнул ли я пост
    val reposts: Reposts?, // Информация о репостах
    val views: Views?, // Информация о просмотрах
    val isPinned: Boolean, // Закреплен ли пост
    val attachments: List<Attachment>?, // Массив объектов с медиавложениями: фото, видео, документы
)