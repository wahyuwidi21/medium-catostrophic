package com.catastrophic.app.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.catastrophic.app.CoroutinesTestRule
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
class DetailPostViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var application: Application

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var viewModel: DetailPostViewModel

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var networkHelper: NetworkHelper

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var response: List<Comment>

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var commentObserver: Observer<Resource<List<Comment>>>
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel =
            DetailPostViewModel(
                postRepository,
                networkHelper, application
            )
    }

    @Test
    fun `given success response when get comments`() = coroutinesTestRule.runBlocking {
        Mockito.`when`(postRepository.getCommentsRemote(1))
            .thenReturn(response)

        viewModel.comments.observeForever(commentObserver)
        viewModel.getComments(1)

        launch(Dispatchers.Main) {
            Mockito.verify(commentObserver).onChanged(Resource.loading(null))
            Mockito.verify(commentObserver).onChanged(Resource.success(response))
        }
        viewModel.comments.removeObserver(commentObserver)
    }

    @Test
    fun `given error response when get comments`() {
        runBlocking {
            val exception = RuntimeException()
            Mockito.`when`(postRepository.getCommentsRemote(1))
                .thenThrow(exception)

            viewModel.comments.observeForever(commentObserver)
            viewModel.getComments(1)

            launch(Dispatchers.Main) {
                Mockito.verify(commentObserver).onChanged(Resource.loading(null))
                Mockito.verify(commentObserver).onChanged(
                    Resource.error(
                        exception.toString(), null,
                        Throwable("", null)
                    )
                )
            }
            viewModel.comments.removeObserver(commentObserver)
        }
    }

    @After
    fun tearDown() {
    }
}