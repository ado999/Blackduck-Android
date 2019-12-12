package pl.edu.wat.wcy.blackduck.data.request

data class EditProfileRequest(
    var profilePicture: String,
    var backgroundPicture: String,
    val fullName: String,
    val description: String,
    val email: String
)