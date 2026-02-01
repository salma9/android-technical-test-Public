package fr.leboncoin.androidrecruitmenttestapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail.DetailsViewModel
import fr.leboncoin.androidrecruitmenttestapp.ui.albumList.AlbumsViewModel
import fr.leboncoin.domain.repository.AlbumRepository

class ViewModelFactory(
    private val repository: AlbumRepository,
    private val albumId: Int? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AlbumsViewModel::class.java) -> {
                AlbumsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> {
                requireNotNull(albumId) { "albumId is required for DetailsViewModel" }
                DetailsViewModel(albumId, repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}