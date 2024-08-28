package ru.netology.nmedia.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.netology.nmedia.data.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun save(post: PostEntity): Long

    @Query("""
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id;
    """)
    fun likeById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query("""
        UPDATE PostEntity SET
        reposts = reposts + 1
        WHERE id = :id
    """)
    fun sharePost(id: Long)

    @Query("""
        UPDATE PostEntity SET
        views = views + 1
        WHERE id = :id
    """)
    fun plusView(id: Long)

    @Update
    fun update(post: PostEntity)
}
