package fr.leboncoin.domain.repository

import fr.leboncoin.domain.AlbumResult
import fr.leboncoin.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun getAlbums(): Flow<AlbumResult<List<Album>>>
    fun getAlbumById(id: Int): Flow<Album?>
    suspend fun toggleFavorite(albumId: Int, currentStatus: Boolean)
}