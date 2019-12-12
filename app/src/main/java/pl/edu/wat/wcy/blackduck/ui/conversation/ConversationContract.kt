package pl.edu.wat.wcy.blackduck.ui.conversation

import pl.edu.wat.wcy.blackduck.data.request.MessageRequest
import pl.edu.wat.wcy.blackduck.data.responses.MessageResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface ConversationContract {

    interface View: BaseContract.View{
        fun onMessagesAvailable(messages: List<MessageResponse>?)
        fun onError(msg: String?)
        fun onMessageSent(sentMessage: MessageResponse?)
    }

    interface Presenter: BaseContract.Presenter<View>{
        fun fetchMessages(messageUsername: String?)
        fun sendMessage(message: MessageRequest)
    }


}