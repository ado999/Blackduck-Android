package pl.edu.wat.wcy.blackduck.data.responses

data class LoginResponse(
    val token: String? = null,
    val user: UserResponse? = null
)