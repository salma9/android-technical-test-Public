package fr.leboncoin.data.repository

import fr.leboncoin.data.database.dao.AlbumDao
import fr.leboncoin.data.database.entity.AlbumEntity
import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.domain.AlbumResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import app.cash.turbine.test
import io.mockk.every


class AlbumRepositoryImplTest {

    private lateinit var repository: AlbumRepositoryImpl
    private val apiService: AlbumApiService = mockk()
    private val albumDao: AlbumDao = mockk(relaxed = true) // relaxed is used to ignore unit fun like insert

    private val albumEntity = AlbumEntity(1, 1, "Title", "url", "thumb")
    private val albumDto = AlbumDto(1, 1, "Title", "url", "thumb")

    @Before
    fun setup() {
        repository = AlbumRepositoryImpl(apiService, albumDao)
    }

    @Test
    fun `getAlbums should emit cache then success after api call`() = runTest {
        // GIVEN
        every { albumDao.getAllAlbums() } returns flowOf(listOf(albumEntity))
        coEvery { apiService.getAlbums() } returns listOf(albumDto)

        // WHEN & THEN
        repository.getAlbums().test {
            val firstEmission = awaitItem()
            assertTrue(firstEmission is AlbumResult.Loading)
            assertEquals(1, firstEmission.data?.size)

            val secondEmission = awaitItem()
            assertTrue(secondEmission is AlbumResult.Success)

            coVerify { albumDao.clearAll() }
            coVerify { albumDao.insertAlbums(any()) }

            awaitComplete()
        }
    }

    @Test
    fun `getAlbums should emit error but keep cache when api fails`() = runTest {
        // GIVEN
        every { albumDao.getAllAlbums() } returns flowOf(listOf(albumEntity))
        coEvery { apiService.getAlbums() } throws Exception("Network Error")

        // WHEN & THEN
        repository.getAlbums().test {
            assertTrue(awaitItem() is AlbumResult.Loading)

            val errorEmission = awaitItem()
            assertTrue(errorEmission is AlbumResult.Error)
            assertEquals(1, errorEmission.data?.size)
            assertEquals("Failed to get album list. Please try again later.", errorEmission.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAlbumById should return mapped domain model`() = runTest {
        // GIVEN
        coEvery { albumDao.getAlbumById(1) } returns flowOf(albumEntity)

        // WHEN & THEN
        repository.getAlbumById(1).test {
            val result = awaitItem()
            assertEquals(albumEntity.title, result?.title)
            awaitComplete()
        }
    }

}