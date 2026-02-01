package fr.leboncoin.androidrecruitmenttestapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.adevinta.spark.components.text.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.androidrecruitmenttestapp.AlbumsViewModel
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.AlbumResult


@SuppressLint("MaterialComposableUsageDetector")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    viewModel: AlbumsViewModel,
    onItemSelected : (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.albums.collectAsStateWithLifecycle()
    val isRefreshing = state is AlbumResult.Loading && !state.data.isNullOrEmpty()
    LaunchedEffect(Unit) { viewModel.loadAlbums() }

    Scaffold(modifier = modifier) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.loadAlbums() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = state) {
                is AlbumResult.Loading -> {
                    val albums = result.data
                    if (albums.isNullOrEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        AlbumList(albums = albums, onItemSelected = onItemSelected)
                    }
                }

                is AlbumResult.Success -> {
                    AlbumList(albums = result.data ?: emptyList(), onItemSelected = onItemSelected)
                }

                is AlbumResult.Error -> {
                    val albums = result.data
                    if (!albums.isNullOrEmpty()) {
                        AlbumList(albums = albums, onItemSelected = onItemSelected)
                    } else {
                        Text(
                            text = result.message ?: "Erreur inconnue",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun AlbumList(
    albums: List<Album>,
    onItemSelected: (Album) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = albums,
            key = { album -> album.id }
        ) { album ->
            AlbumItem(
                album = album,
                onItemSelected = onItemSelected,
            )
        }
    }
}