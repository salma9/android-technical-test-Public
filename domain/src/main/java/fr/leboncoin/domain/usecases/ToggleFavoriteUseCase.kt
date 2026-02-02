package fr.leboncoin.domain.usecases

import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(album: Album) {
        repository.toggleFavorite(album.id, !album.isFavorite)
    }
}