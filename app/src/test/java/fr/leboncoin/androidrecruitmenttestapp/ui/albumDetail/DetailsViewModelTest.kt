package fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import fr.leboncoin.domain.AlbumResult
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
        thumbnailUrl = "thumb"
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
        val repository = object : AlbumRepository {
            override fun getAlbumById(id: Int): Flow<Album?> {
                return if (id == 42) flowOf(album) else flowOf(null)
            }
            override fun getAlbums(): Flow<AlbumResult<List<Album>>> = throw Exception("Not used")
        }

        val savedStateHandle = SavedStateHandle(mapOf("ALBUM_ID" to 42))
        val viewModel = DetailsViewModel(savedStateHandle = savedStateHandle, repository = repository)

        viewModel.album.test {
            assertEquals(null, awaitItem())

            val result = awaitItem()
            assertEquals(album.id, result?.id)
            assertEquals("Test Album", result?.title)

            cancelAndIgnoreRemainingEvents()
        }
    }
}