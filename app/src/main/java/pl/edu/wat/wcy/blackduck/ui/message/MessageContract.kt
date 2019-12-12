package pl.edu.wat.wcy.blackduck.ui.message

import pl.edu.wat.wcy.blackduck.data.responses.ConversationResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface MessageContract {

    interface View : BaseContract.View {
        fun onError(msg: String?)
        fun onConversationsAvailable(conversations: List<ConversationResponse>?)

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onConversationsRefresh()

    }
}