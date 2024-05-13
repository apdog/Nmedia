package ru.netology.nmedia.post

import ru.netology.nmedia.post.attachments.Attachment
import java.util.Date

data class Post(
    var id: Int, // Уникальный номер поста
    val fromId: Int, // ID автора поста
    var date: Date, // Дата публикации поста
    val title: String, // заголовок поста
    val text: String?, // Текст поста
    val friendsOnly: Boolean, // если пост виден только друзьям
    var comments: MutableList<Comments>? = mutableListOf(), // Информация о комментариях
    var likes: Likes?, // Информация о лайках
    var likedByMe: Boolean = false, // Лайкнул ли я пост
    var reposts: Reposts?, // Информация о репостах
    var views: Views?, // Информация о просмотрах
    val isPinned: Boolean, // Закреплен ли пост
    val attachments: List<Attachment>?, // Массив объектов с медиавложениями: фото, видео, документы
)