package com.catastrophic.app.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.catastrophic.app.CoroutinesTestRule
import com.catastrophic.app.ui.viewmodel.ListPostViewModel
import com.catastrophic.app.utils.NetworkHelper
import com.catastrophic.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
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
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ListPostViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var application: Application

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var viewModel: ListPostViewModel

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var networkHelper: NetworkHelper

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var response: List<PostFinal>

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var postObserver: Observer<Resource<List<PostFinal>>>
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel =
            ListPostViewModel(
                postRepository,
                networkHelper, application
            )
    }

    @Test
    fun `given success response when get posts`() = coroutinesTestRule.runBlocking {
        Mockito.`when`(postRepository.getListPostData())
            .thenReturn(response)

        viewModel.postData.observeForever(postObserver)
        viewModel.getPosts()

        launch(Dispatchers.Main) {
            Mockito.verify(postObserver).onChanged(Resource.loading(null))
            Mockito.verify(postObserver).onChanged(Resource.success(response))
        }
        viewModel.postData.removeObserver(postObserver)
    }

    @Test
    fun `given error response when get posts`() {
        runBlocking {
            val exception = RuntimeException()
            Mockito.`when`(postRepository.getListPostData())
                .thenThrow(exception)

            viewModel.postData.observeForever(postObserver)
            viewModel.getPosts()

            launch(Dispatchers.Main) {
                Mockito.verify(postObserver).onChanged(Resource.loading(null))
                Mockito.verify(postObserver).onChanged(
                    Resource.error(
                        exception.toString(), null,
                        Throwable("", null)
                    )
                )
            }
            viewModel.postData.removeObserver(postObserver)
        }
    }

    @After
    fun tearDown() {
    }
}