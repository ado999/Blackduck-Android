package pl.edu.wat.wcy.blackduck.data.request

data class SignupRequest(
    var username: String? = null,
    var fullName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var description: String = "")