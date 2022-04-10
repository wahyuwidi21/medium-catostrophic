package com.catastrophic.app.viewmodel

import android.app.Application
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class PhotoViewModelTest {

    @Mock
    private lateinit var application: Application

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var viewModel: PhotoDetailViewModel
    @Before
    fun setUp() {
        viewModel =
            PhotoDetailViewModel(
                 application
            )
    }

    @Test
    fun `test empty view model`() {
      val vm= viewModel
        assertEquals(viewModel,vm)
    }
}