package ru.netology.nmedia.domain.post

data class Post(
    val id: Long = 0, // Уникальный номер поста
    val author: String?, // автор поста
    val content: String?, // Текст поста
    val published: Long, // Дата публикации поста
    val likedByMe: Boolean, // Лайкнул ли я пост
    val likes: Int = 0, // Информация о лайках
    val attachment: Attachment?, //Вложение: фото, видео, документы
    val title: String, // Заголовок поста
    val authorAvatar: String? = null // Аватарка
//    val friendsOnly: Boolean, // Если пост виден только друзьям
//    val comments: Int = 0, // Информация о комментариях
//    val reposts: Int, // Информация о репостах
//    val views: Int, // Информация о просмотрах
//    val isPinned: Boolean, // Закреплен ли пост
)
