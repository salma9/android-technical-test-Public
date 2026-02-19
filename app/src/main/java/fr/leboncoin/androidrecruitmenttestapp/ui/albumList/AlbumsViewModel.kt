package fr.leboncoin.androidrecruitmenttestapp.ui.albumList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import fr.leboncoin.domain.AlbumResult
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.usecases.GetAlbumsUseCase
import fr.leboncoin.domain.usecases.UpdateFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
) : ViewModel() {

    private val _albums = MutableStateFlow<AlbumResult<List<Album>>>(AlbumResult.Loading())
    val albums: StateFlow<AlbumResult<List<Album>>> = _albums.asStateFlow()

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            getAlbumsUseCase().collect { result ->
                _albums.value = result
            }
        }
    }
    fun onFavoriteClicked(album: Album) {
        viewModelScope.launch {
            Log.d("FAV", "Clic sur l'album ${album.id}, ancien état: ${album.isFavorite}")
            try {
                updateFavoriteUseCase(album)
                 analyticsHelper.trackSelection("favorite_toggled ${album.id}")
            } catch (e: Exception) {
                Log.e("AlbumsViewModel", "Erreur lors du switch favori", e)
            }
        }
    }
}