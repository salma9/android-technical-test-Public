package fr.leboncoin.data.repository

import android.util.Log
import fr.leboncoin.data.database.dao.AlbumDao
import fr.leboncoin.data.database.entity.AlbumEntity
import fr.leboncoin.data.mapper.toAlbum
import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.domain.AlbumResult
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val albumApiService: AlbumApiService,
    private val albumDao: AlbumDao
): AlbumRepository {

    override fun getAlbums(): Flow<AlbumResult<List<Album>>> = flow {
        // check local storage
        val localFlow = albumDao.getAllAlbums().map { entities ->
            AlbumResult.Success(entities.map { it.toAlbum() })
        }

        // send initial data
        val initialData = albumDao.getAllAlbums().first()
        emit(AlbumResult.Loading(data = initialData.map { it.toAlbum() }))

        try {
            // check remote data
            val remoteAlbums = albumApiService.getAlbums()

            // check If Album is already in favorites
            val favoritesIds = initialData.filter { it.isFavorite }.map { it.id }.toSet()

            // Mapping
            val entities = remoteAlbums.map { dto ->
                AlbumEntity(
                    id = dto.id,
                    albumId = dto.albumId,
                    title = dto.title,
                    url = dto.url,
                    thumbnailUrl = dto.thumbnailUrl,
                    isFavorite = favoritesIds.contains(dto.id)
                )
            }

            // update DB
            albumDao.insertAlbums(entities)

            // emit local flow
            emitAll(localFlow)

        } catch (e: Exception) {
            emit(AlbumResult.Error(
                message = "Failed to get album list. Please try again later.",
                data = initialData.map { it.toAlbum() }
            ))
            Log.e("AlbumRepository", "Network fetch failed, falling back to cache", e)

        }
    }

    override fun getAlbumById(id: Int): Flow<Album?> = albumDao.getAlbumById(id).map { it?.toAlbum() }

    override suspend fun updateFavoriteStatus(albumId: Int, currentStatus: Boolean) {
        albumDao.toggleFavorite(albumId, currentStatus)
    }
}
