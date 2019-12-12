package pl.edu.wat.wcy.blackduck.data.responses

import java.util.*

data class UserShortResponse(
    val username: String,
    val fullName: String,
    val profilePhotoUrl: String,
    val profileThumbnail: String,
    val lastActivity: Date
)