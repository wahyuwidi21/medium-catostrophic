package com.catastrophic.app.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catastrophic.app.CoroutinesTestRule
import com.catastrophic.app.data.model.Cat
import com.catastrophic.app.data.source.ApiServiceCat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var catRepository: CatRepository

    @Mock
    private lateinit var apiServiceUser: ApiServiceCat

    @Mock
    private lateinit var listAlbumResponse: List<Album>

    @Mock
    private lateinit var userResponse: Cat

    @Mock
    private lateinit var listPhotoResponse: List<Photo>

    @Mock
    private lateinit var listAlbumWithPhotos: List<HashMap<Album, List<Photo>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        apiServiceUser = Mockito.mock(ApiServiceCat::class.java, Mockito.RETURNS_DEEP_STUBS)
        listAlbumWithPhotos =
            listOf(hashMapOf(Pair(Album(1, 1, ""), listOf(Photo(1, 1, "", "", "")))))
        listAlbumResponse = listOf(Album(1, 1, ""))
        listPhotoResponse = listOf(Photo(1, 1, "", "", ""))
        userResponse =
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

        catRepository =
            CatRepository(apiServiceUser)

    }

    //
    @Test
    fun `given success response when get user remote`() {
        runBlocking {
            Mockito.`when`(apiServiceUser.getUserAsync(1).await())
                .thenReturn(userResponse)

            val data = catRepository.getUserRemote(1)

            assertEquals(userResponse, data)
        }
    }

    @Test
    fun `given success response when get album remote`() {
        runBlocking {
            Mockito.`when`(apiServiceUser.getUserAlbumsAsync(1).await())
                .thenReturn(listAlbumResponse)

            val data = catRepository.getUserAlbums(1)

            assertEquals(listAlbumResponse, data)
        }
    }

    @Test
    fun `given success response when get photos remote`() {
        runBlocking {
            Mockito.`when`(apiServiceUser.getAlbumPhotosAsync(1).await())
                .thenReturn(listPhotoResponse)

            val data = catRepository.getUserPhotos(1)

            assertEquals(listPhotoResponse, data)
        }
    }

    @Test
    fun `given success response when get album with photos remote`() {
        runBlocking {
            Mockito.`when`(apiServiceUser.getUserAlbumsAsync(1).await())
                .thenReturn(listAlbumResponse)
//
            Mockito.`when`(apiServiceUser.getAlbumPhotosAsync(1).await())
                .thenReturn(listPhotoResponse)

            val fixData = catRepository.getAlbumsWithPhotos(1) as List<HashMap<Album, List<Photo>>>

            assertEquals(listAlbumWithPhotos, fixData)
        }
    }
}