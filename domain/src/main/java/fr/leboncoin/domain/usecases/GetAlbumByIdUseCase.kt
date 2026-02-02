package fr.leboncoin.domain.usecases

import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumByIdUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke(albumId: Int): Flow<Album?> {
        return repository.getAlbumById(albumId)
    }
}