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
        val cache = albumDao.getAllAlbums().first().map { it.toAlbum() }
        emit(AlbumResult.Loading(data = cache))

        try {
            // check data on remote
            val remoteAlbums = albumApiService.getAlbums()

            // mapping
            val entities = remoteAlbums.map { dto ->
                AlbumEntity(
                    id = dto.id,
                    albumId = dto.albumId,
                    title = dto.title,
                    url = dto.url,
                    thumbnailUrl = dto.thumbnailUrl
                )
            }

            albumDao.clearAll()
            albumDao.insertAlbums(entities)

            // emit success result
            val updatedCache = albumDao.getAllAlbums().first().map { it.toAlbum() }
            emit(AlbumResult.Success(updatedCache))

        } catch (e: Exception) {
            // emit error result
            Log.e("AlbumRepositoryImpl", "getAlbums: ", e)
            emit(AlbumResult.Error(
                message = "Failed to get album list. Please try again later.",
                data = cache
            ))
        }
    }

    override fun getAlbumById(id: Int): Flow<Album?> = albumDao.getAlbumById(id).map { it?.toAlbum() }
}