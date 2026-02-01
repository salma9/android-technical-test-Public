package fr.leboncoin.androidrecruitmenttestapp.ui.albumList

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
        val album = Album(id = 1, albumId = 1, title = "Test", url = "test", thumbnailUrl = "test")
        val repository = object : AlbumRepository {
            override fun getAlbums(): Flow<AlbumResult<List<Album>>> =
                flowOf(AlbumResult.Success(listOf(album)))

            override fun getAlbumById(id: Int): Flow<Album?> = throw Exception("Not used")
        }

        val viewModel = AlbumsViewModel(repository)

        viewModel.albums.test {
            val firstItem = awaitItem()
            Assert.assertTrue(firstItem is AlbumResult.Loading)

            viewModel.loadAlbums()

            val secondItem = awaitItem()
            Assert.assertTrue(secondItem is AlbumResult.Success)
            Assert.assertTrue(secondItem.data?.isNotEmpty() == true)

            cancelAndIgnoreRemainingEvents()
        }
    }
}