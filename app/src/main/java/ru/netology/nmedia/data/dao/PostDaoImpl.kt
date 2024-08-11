package ru.netology.nmedia.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.domain.post.Post
import java.util.Date

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_FROM_ID} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.DATE} TEXT NOT NULL,
            ${PostColumns.TITLE} TEXT NOT NULL,
            ${PostColumns.TEXT} TEXT NOT NULL,
            ${PostColumns.FRIENDS_ONLY} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COMMENTS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.REPOSTS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.VIEWS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.IS_PINNED} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.ATTACHMENTS} BOOLEAN NOT NULL DEFAULT 0
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_FROM_ID = "fromId"
        const val DATE = "date"
        const val TITLE = "title"
        const val TEXT = "text"
        const val FRIENDS_ONLY = "friendsOnly"
        const val COMMENTS = "comments"
        const val LIKES = "likes"
        const val LIKED_BY_ME = "likedByMe"
        const val REPOSTS = "reposts"
        const val VIEWS = "views"
        const val IS_PINNED = "isPinned"
        const val ATTACHMENTS = "attachments"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_FROM_ID,
            DATE,
            TITLE,
            TEXT,
            FRIENDS_ONLY,
            COMMENTS,
            LIKES,
            LIKED_BY_ME,
            REPOSTS,
            VIEWS,
            IS_PINNED,
            ATTACHMENTS
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_FROM_ID, post.fromId)
            put(PostColumns.DATE, post.date.toString())
            put(PostColumns.TITLE, "Me")
            put(PostColumns.TEXT, post.text)
            put(PostColumns.FRIENDS_ONLY, post.friendsOnly)
            put(PostColumns.COMMENTS, 0)
            put(PostColumns.LIKES, post.likes)
            put(PostColumns.LIKED_BY_ME, post.likedByMe)
            put(PostColumns.REPOSTS, post.reposts)
            put(PostColumns.VIEWS, post.views)
            put(PostColumns.IS_PINNED, post.isPinned)
            put(PostColumns.ATTACHMENTS, 0)
        }

        val id: Long = if (post.id != 0) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString())
            )
            post.id.toLong()
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }

        // Проверяем, что строка с данным ID существует
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            return if (cursor.moveToFirst()) {
                map(cursor)
            } else {
                // Обработка случая, когда пост с данным ID не найден
                throw IllegalStateException("Post with ID $id not found after saving.")
            }
        }
    }

    override fun likeById(id: Int) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Int) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun sharePost(id: Int) {
        db.execSQL(
            """
            UPDATE ${PostColumns.TABLE} SET
                ${PostColumns.REPOSTS} = ${PostColumns.REPOSTS} + 1
            WHERE ${PostColumns.COLUMN_ID} = ?;
            """.trimIndent(), arrayOf(id.toString())
        )
    }

    override fun plusView(id: Int) {
        db.execSQL(
            """
            UPDATE ${PostColumns.TABLE} SET
                ${PostColumns.VIEWS} = ${PostColumns.VIEWS} + 1
            WHERE ${PostColumns.COLUMN_ID} = ?;
            """.trimIndent(), arrayOf(id.toString())
        )
    }

    private fun map(cursor: Cursor): Post {
        return Post(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
            fromId = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.COLUMN_FROM_ID)),
            date = Date(cursor.getString(cursor.getColumnIndexOrThrow(PostColumns.DATE))),
            title = cursor.getString(cursor.getColumnIndexOrThrow(PostColumns.TITLE)),
            text = cursor.getString(cursor.getColumnIndexOrThrow(PostColumns.TEXT)),
            friendsOnly = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.FRIENDS_ONLY)) != 0,
            comments = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.COMMENTS)),
            likes = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.LIKES)),
            likedByMe = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.LIKED_BY_ME)) != 0,
            reposts = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.REPOSTS)),
            views = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.VIEWS)),
            isPinned = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.IS_PINNED)) != 0,
            attachments = null
        )
    }

}