package fr.leboncoin.androidrecruitmenttestapp.ui.albumList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.chips.ChipTinted
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.text.Text
import fr.leboncoin.androidrecruitmenttestapp.ui.components.RemoteImage
import fr.leboncoin.domain.model.Album

@OptIn(ExperimentalSparkApi::class)
@Composable
fun AlbumItem(
    album: Album,
    onItemSelected: (Album) -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp),
        onClick = { onItemSelected(album) },
    ) {
        Row {
            RemoteImage(
                url = album.thumbnailUrl,
                contentDescription = album.title,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                Text(
                    text = album.title,
                    style = SparkTheme.typography.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        ChipTinted(text = "Album #${album.albumId}")
                        ChipTinted(text = "Track #${album.id}")
                    }

                    IconButton(
                        onClick = onFavoriteClick,
                    ) {
                        Icon(
                            imageVector = if (album.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (album.isFavorite) SparkTheme.colors.error else SparkTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}