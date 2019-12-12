package pl.edu.wat.wcy.blackduck.data.preference

data class StoredUser(
    val token: String,
    val uuid: String,
    val displayName: String,
    var fullname: String,
    val creationDate: String,
    var profilePhotoUrl: String,
    var profileBackgroundUrl: String,
    var description: String,
    var email: String
)