package pl.edu.wat.wcy.blackduck.data.responses

import java.util.*

data class MessageResponse(
    val id: Int,
    val message: String,
    val fromUser: UserShortResponse,
    val toUser: UserShortResponse,
    val date: Date
)