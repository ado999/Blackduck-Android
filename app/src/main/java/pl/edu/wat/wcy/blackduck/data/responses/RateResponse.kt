package pl.edu.wat.wcy.blackduck.data.responses

data class RateResponse(
    val rate: Int,
    val fromUser: UserShortResponse
)