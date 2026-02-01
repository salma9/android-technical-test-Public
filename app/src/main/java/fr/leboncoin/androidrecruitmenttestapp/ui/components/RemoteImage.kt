package fr.leboncoin.androidrecruitmenttestapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import fr.leboncoin.androidrecruitmenttestapp.R

@Composable
fun RemoteImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .httpHeaders(
                NetworkHeaders.Builder()
                    .add("User-Agent", "LeboncoinApp/1.0")
                    .build()
            )
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        placeholder = painterResource(R.drawable.baseline_error_outline_24),
        error = painterResource(R.drawable.baseline_error_outline_24),
        modifier = modifier,
        contentScale = contentScale
    )
}