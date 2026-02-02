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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AlbumRepositoryImpl(
    private val albumApiService: AlbumApiService,
    private val albumDao: AlbumDao
): AlbumRepository {

    override fun getAlbums(): Flow<AlbumResult<List<Album>>> = flow {
        // check data on local mode
        val localAlbums = albumDao.getAllAlbums().first().map { it.toAlbum() }
        emit(AlbumResult.Loading(data = localAlbums))

        try {
            // check data on remote
            val remoteAlbums = albumApiService.getAlbums()

            //get favorite albums ID
            val favoritesIds = localAlbums.filter { it.isFavorite }.map { it.id }.toSet()

            // mapping
            val entitiesToInsert = remoteAlbums.map { dto ->
                AlbumEntity(
                    id = dto.id,
                    albumId = dto.albumId,
                    title = dto.title,
                    url = dto.url,
                    thumbnailUrl = dto.thumbnailUrl,
                    isFavorite = favoritesIds.contains(dto.id) // check if album is favorite
                )
            }

            albumDao.insertAlbums(entitiesToInsert)

            // emit success result
            val updatedCache = albumDao.getAllAlbums().first().map { it.toAlbum() }
            emit(AlbumResult.Success(updatedCache))

        } catch (e: Exception) {
            // emit error result
            emit(AlbumResult.Error(
                message = "Failed to get album list. Please try again later.",
                data = localAlbums
            ))
            Log.e("AlbumRepository", "getAlbums fail: $e")
        }
    }

    override fun getAlbumById(id: Int): Flow<Album?> = albumDao.getAlbumById(id).map { it?.toAlbum() }

    override suspend fun toggleFavorite(albumId: Int, isFavorite: Boolean) {
        albumDao.toggleFavorite(albumId, isFavorite)
    }
}