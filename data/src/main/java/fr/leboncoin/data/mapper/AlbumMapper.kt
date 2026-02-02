package fr.leboncoin.data.mapper

import fr.leboncoin.data.database.entity.AlbumEntity
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.domain.model.Album

//API -> DB
fun AlbumDto.toEntity(): AlbumEntity = AlbumEntity(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl
)

// DB -> Album
fun AlbumEntity.toAlbum(): Album = Album(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
    isFavorite = isFavorite
)