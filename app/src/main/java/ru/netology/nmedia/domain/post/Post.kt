package ru.netology.nmedia.domain.post

import ru.netology.nmedia.domain.post.attachments.Attachment
import java.util.Date

data class Post(
    val id: Long, // Уникальный номер поста
    val fromId: Long, // ID автора поста
    val date: Date, // Дата публикации поста
    val title: String, // Заголовок поста
    val text: String?, // Текст поста
    val friendsOnly: Boolean, // Если пост виден только друзьям
    val comments: Int = 0, // Информация о комментариях
    val likes: Int, // Информация о лайках
    val likedByMe: Boolean = false, // Лайкнул ли я пост
    val reposts: Int, // Информация о репостах
    val views: Int, // Информация о просмотрах
    val isPinned: Boolean, // Закреплен ли пост
    val attachments: List<Attachment>?, // Массив объектов с медиавложениями: фото, видео, документы
)
