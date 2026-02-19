package fr.leboncoin.domain.usecases

import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import javax.inject.Inject

class UpdateFavoriteUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(album: Album) {
        repository.updateFavoriteStatus(album.id, !album.isFavorite)
    }
}