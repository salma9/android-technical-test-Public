package fr.leboncoin.androidrecruitmenttestapp.ui.albumList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.domain.AlbumResult
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumsViewModel(
    private val repository: AlbumRepository,
) : ViewModel() {

    private val _albums = MutableStateFlow<AlbumResult<List<Album>>>(AlbumResult.Loading())
    val albums: StateFlow<AlbumResult<List<Album>>> = _albums.asStateFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            repository.getAlbums().collect { result ->
                _albums.value = result
            }
        }
    }
}