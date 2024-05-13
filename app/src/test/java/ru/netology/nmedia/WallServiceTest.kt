import ru.netology.nmedia.post.WallService.add
import ru.netology.nmedia.post.WallService.clearComments
import ru.netology.nmedia.post.WallService.clearPosts
import ru.netology.nmedia.post.WallService.update
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.netology.nmedia.post.Comments
import ru.netology.nmedia.post.Likes
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.post.PostNotFoundException
import ru.netology.nmedia.post.Reposts
import ru.netology.nmedia.post.Views
import ru.netology.nmedia.post.WallService
import java.util.*

class WallServiceTest {
    @Test
    fun addPostIncrementsId() {
        // Создаем тестовый пост
        val post = Post(
            1,
            1,
            Date(),
            "Test title",
            "Test post",
            false,
            comments = mutableListOf(Comments(0, "комментарий")),
            Likes(0, 0, false),
            likedByMe = false,
            Reposts(0, false),
            Views(0),
            false,
            ArrayList(),
        )

        // Добавляем пост
        add(post)

        // Проверяем, что ID поста не равен 0
        assertNotEquals(0, post.id)
    }

    @Test
    fun updateExistingPostReturnsTrue() {
        // Создаем два поста с одинаковым ID
        val post1 = Post(
            1,
            1,
            Date(),
            "Test title 1",
            "Test post 1",
            false,
            comments = mutableListOf(Comments(0, "комментарий")),
            Likes(0, 0, false),
            likedByMe = false,
            Reposts(0, false),
            Views(0),
            false,
            ArrayList(),
        )
        val post2 = Post(
            1,
            1,
            Date(),
            "Test title 1",
            "Updated test post 1",
            false,
            comments = mutableListOf(Comments(0, "комментарий")),
            Likes(0, 0, false),
            likedByMe = false,
            Reposts(0, false),
            Views(0),
            false,
            ArrayList(),
        )

        add(post1)
        add(post2)

        // Обновляем второй пост
        val updated = update(post2)

        // Проверяем, что метод update вернул true
        assertTrue(updated)
    }

    @Test
    fun updateNonExistingPostReturnsFalse() {
        // Создаем пост с несуществующим ID
        val post = Post(
            100,
            1,
            Date(),
            "Test title",
            "Test post",
            false,
            comments = mutableListOf(Comments(0, "комментарий")),
            Likes(0, 0, false),
            likedByMe = false,
            Reposts(0, false),
            Views(0),
            false,
            ArrayList(),
        )

        // Обновляем пост
        val updated = update(post)

        // Проверяем, что метод update вернул false
        assertFalse(updated)
    }

    @Test
    fun shouldAddCommentToExistingPost() {
        // Создаем пост
        val post = Post(
            id = 1,
            fromId = 1,
            date = Date(),
            "Test title",
            text = "Test post",
            friendsOnly = false,
            comments = mutableListOf(),
            likes = Likes(0, 0, false),
            reposts = Reposts(0, false),
            views = Views(0),
            attachments = emptyList(),
            isPinned = false,
        )

        // Добавляем пост
        val addedPost = WallService.add(post)

        // Создаем комментарий
        val comment = Comments(
            id = 1,
            text = "Test comment"
        )

        // Добавляем комментарий к существующему посту
        WallService.createComment(addedPost.id, comment)

        // Проверяем, что комментарий добавлен к посту
        val updatedPost = WallService.getPostById(addedPost.id)
        assertEquals(1, updatedPost?.comments?.size)
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrowExceptionForNonExistingPost() {
        // Пытаемся добавить комментарий к несуществующему посту
        val comment = Comments(
            id = 1,
            text = "Test comment"
        )
        WallService.createComment(100, comment)
    }

    @Before
    fun clearBeforeTest() {
        // Очищаем список постов перед каждым тестом
        clearPosts()
        clearComments()
    }
}