package fr.leboncoin.androidrecruitmenttestapp.ui.albumList

import app.cash.turbine.test
import fr.leboncoin.domain.AlbumResult
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.usecases.GetAlbumsUseCase
import fr.leboncoin.domain.usecases.ToggleFavoriteUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun down() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadsAlbums_emitsNonEmptyList() = runTest {

        //GIVEN
        val album = Album(id = 1, albumId = 1, title = "Test", url = "test", thumbnailUrl = "test", isFavorite = false)
        val albumTwo = Album(id = 2, albumId = 2, title = "Test", url = "test", thumbnailUrl = "test", isFavorite = false)
        val getAlbumsUseCase = mockk<GetAlbumsUseCase>()
        val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()

        every { getAlbumsUseCase() } returns flow {
            emit(AlbumResult.Loading(emptyList()))
            emit(AlbumResult.Success(listOf(album, albumTwo)))
        }

        //WHEN
        val viewModel = AlbumsViewModel(getAlbumsUseCase, toggleFavoriteUseCase)

        //THEN
        viewModel.albums.test {
            val loadingState = awaitItem()
            Assert.assertTrue(loadingState is AlbumResult.Loading) //initial value
            val next = awaitItem()

            val successState = next as? AlbumResult.Success ?: awaitItem()
            assertTrue(successState is AlbumResult.Success)
            assertEquals(2, successState.data?.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}