package fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail.DetailsActivity.Companion.EXTRA_ALBUM_ID
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.usecases.GetAlbumByIdUseCase
import fr.leboncoin.domain.usecases.UpdateFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
) : ViewModel() {

    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album.asStateFlow()

    private val albumId: Int = savedStateHandle.get<Int>(EXTRA_ALBUM_ID) ?: -1

    init {
        loadAlbumDetails()
    }

    private fun loadAlbumDetails() {
        viewModelScope.launch {
            getAlbumByIdUseCase(albumId).collect {
                _album.value = it
            }
        }
    }

    fun onFavoriteClicked() {
        val currentAlbum = _album.value ?: return
        viewModelScope.launch {
            updateFavoriteUseCase(currentAlbum)
        }
    }

}