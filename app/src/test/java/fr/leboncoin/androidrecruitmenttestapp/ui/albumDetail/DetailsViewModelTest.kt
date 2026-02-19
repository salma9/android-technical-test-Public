package fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.usecases.GetAlbumByIdUseCase
import fr.leboncoin.domain.usecases.UpdateFavoriteUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val album = Album(
        id = 42,
        albumId = 1,
        title = "Test Album",
        url = "url",
        thumbnailUrl = "thumb",
        isFavorite = false
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized, fetches album details for given id`() = runTest {
        val getAlbumByIdUseCase = mockk<GetAlbumByIdUseCase>()
        val updateFavoriteUseCase = mockk<UpdateFavoriteUseCase>()

        every { getAlbumByIdUseCase(42) } returns flowOf(album)

        val savedStateHandle = SavedStateHandle(mapOf("ALBUM_ID" to 42))
        val viewModel = DetailsViewModel(savedStateHandle, getAlbumByIdUseCase, updateFavoriteUseCase)

        viewModel.album.test {
            assertEquals(null, awaitItem())

            val result = awaitItem()
            assertEquals(album.id, result?.id)
            assertEquals("Test Album", result?.title)

            cancelAndIgnoreRemainingEvents()
        }
    }
}