package fr.leboncoin.androidrecruitmenttestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.leboncoin.domain.AlbumResult
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class AlbumsViewModel(
    private val repository: AlbumRepository,
) : ViewModel() {

    private val _albums = MutableStateFlow<AlbumResult<List<Album>>>(AlbumResult.Loading())
    val albums: StateFlow<AlbumResult<List<Album>>> = _albums

    fun loadAlbums() {
        viewModelScope.launch {
            repository.getAlbums().collect { result ->
                _albums.value = result
            }
        }
    }

    class Factory(
        private val repository: AlbumRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlbumsViewModel(repository) as T
        }
    }
}