package com.rezahosseini.taskmanager.data.remote.vm

import app.cash.turbine.test
import com.rezahosseini.taskmanager.testutil.MainDispatcherRule
import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.repository.DataImageProvider
import com.rezahosseini.taskmanager.vm.ViewModelDataImage
import com.rezahosseini.taskmanager.vm.state.UiState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ViewModelDataImageTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var provider: DataImageProvider
    private lateinit var viewModel: ViewModelDataImage

    private val images = listOf(
        DataImage(1L, "icons/icon1.png"),
        DataImage(2L, "icons/icon2.png")
    )

    @Before
    fun setup() {
        provider = mock()

        whenever(provider.getImages())
            .thenReturn(flowOf(images))

        viewModel = ViewModelDataImage(provider)
    }

    @Test
    fun getImages_emitsLoadingAndSuccess() = runTest {
        viewModel.getImages.test {
            assertEquals(
                UiState.Loading,
                awaitItem()
            )

            val result = awaitItem()

            assertTrue(result is UiState.Success)

            result as UiState.Success

            assertEquals(images, result.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getImages_emitsErrorWhenProviderFails() = runTest {
        whenever(provider.getImages())
            .thenReturn(
                flow {
                    throw IllegalStateException("Network error")
                }
            )

        viewModel = ViewModelDataImage(provider)

        viewModel.getImages.test {
            assertEquals(
                UiState.Loading,
                awaitItem()
            )

            val result = awaitItem()

            assertTrue(result is UiState.Error)

            result as UiState.Error

            assertEquals(
                "Network error",
                result.message
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getImages_emitsSuccessWithEmptyList() = runTest {
        whenever(provider.getImages())
            .thenReturn(flowOf(emptyList()))

        viewModel = ViewModelDataImage(provider)

        viewModel.getImages.test {
            assertEquals(
                UiState.Loading,
                awaitItem()
            )

            val result = awaitItem()

            assertTrue(result is UiState.Success)

            result as UiState.Success

            assertEquals(emptyList<DataImage>(), result.data)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
