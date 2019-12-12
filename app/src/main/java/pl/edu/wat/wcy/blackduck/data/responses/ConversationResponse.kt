package pl.edu.wat.wcy.blackduck.data.responses

data class ConversationResponse(
    val cid: String,
    val user1: UserShortResponse,
    val user2: UserShortResponse
)