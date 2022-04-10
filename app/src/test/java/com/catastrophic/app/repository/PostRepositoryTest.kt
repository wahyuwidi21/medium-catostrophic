package com.catastrophic.app.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catastrophic.app.CoroutinesTestRule
import com.catastrophic.app.data.model.Cat
import com.catastrophic.app.data.source.ApiServiceCat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*
import org.mockito.*

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class PostRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var apiServiceUser: ApiServiceCat

    @Mock
    private lateinit var apiServicePost: ApiServicePost

    @Mock
    private lateinit var listPostResponse: List<Post>

    @Mock
    private lateinit var listUserResponse: List<Cat>

    @Mock
    private lateinit var listCommentResponse: List<Comment>

    @Mock
    private lateinit var listPostFinal: List<PostFinal>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        apiServicePost = Mockito.mock(ApiServicePost::class.java, Mockito.RETURNS_DEEP_STUBS)
        apiServiceUser = Mockito.mock(ApiServiceCat::class.java, Mockito.RETURNS_DEEP_STUBS)
        listPostFinal = listOf(PostFinal(1, "test nama", 1, "company", "title", "body"))
        listPostResponse = listOf(Post(1, 1, "title", "body"))
        listUserResponse = listOf(
            Cat(
                1,
                "test nama",
                "username test",
                "email test",
                Cat.Address(
                    "jalan test", "suite test", "city test", "zipcode test",
                    Cat.Address.Geo("lat", "lng")
                ), "phone", "website", Cat.Company("company", "cp", "bs")
            )
        )
        listCommentResponse = listOf(Comment(1,1,"","",""))
        postRepository =
            PostRepository(apiServicePost, apiServiceUser)

    }

    //
    @Test
    fun `given success response when get posts remote`() {
        runBlocking {

            Mockito.`when`(apiServicePost.getPostsAsync().await())
                .thenReturn(listPostResponse)
            Mockito.`when`(apiServiceUser.getUsersAsync().await())
                .thenReturn(listUserResponse)
            val data = postRepository.getListPostData()

            assertEquals(listPostFinal, data)
        }
    }

    @Test
    fun `given success response when get comments remote`() {
        runBlocking {
            Mockito.`when`(apiServicePost.getPostCommentsAsync(1).await())
                .thenReturn(listCommentResponse)

            val data = postRepository.getCommentsRemote(1)

            assertEquals(listCommentResponse, data)
        }
    }
}