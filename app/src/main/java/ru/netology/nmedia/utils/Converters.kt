package ru.netology.nmedia.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.domain.post.attachments.Attachment
import java.util.*

object Converters {

    // Конвертер для Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Конвертер для List<Attachment>
    @TypeConverter
    fun fromAttachmentsList(value: List<Attachment>?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toAttachmentsList(value: String?): List<Attachment>? {
        return value?.let {
            val listType = object : TypeToken<List<Attachment>>() {}.type
            Gson().fromJson(it, listType)
        }
    }
}