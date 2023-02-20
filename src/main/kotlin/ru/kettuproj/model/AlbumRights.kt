package ru.kettuproj.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumRights(
    var canRename: Boolean = false,
    var canChangeIcon: Boolean = false,
    var canInvite: Boolean = false,
    var canAddImage: Boolean = false,
    var canSeeImage: Boolean = false,
    var canDeleteImage: Boolean = false
)
