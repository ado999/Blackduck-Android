package pl.edu.wat.wcy.blackduck.data.responses

import java.util.ArrayList
import java.util.Date

data class UserResponse(
    val uuid: String? = null,
    val displayName: String? = null,
    val fullName: String? = null,
    val creationDate: Date? = null,
    val profilePhotoUrl: String? = null,
    val profileThumbnail: String? = null,
    val profileBackgroundUrl: String? = null,
    val description: String? = null,
    val email: String? = null,
    val lastActivity: Date? = null,
    val followed: Boolean = false
    )