package pl.edu.wat.wcy.blackduck.ui.conversation

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.GetMessagesRequest
import pl.edu.wat.wcy.blackduck.data.request.MessageRequest
import javax.inject.Inject

class ConversationPresenter: ConversationContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    lateinit var view: ConversationContract.View

    private var subscription: Disposable? = null

    override fun fetchMessages(messageUsername: String?) {
        subscription = portalApi
            .getMessages(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                GetMessagesRequest(messageUsername!!)
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onMessagesAvailable(it)
                }, {
                    view.onError(it.message)
                }
            )
    }

    override fun sendMessage(message: MessageRequest) {
        subscription = portalApi
            .sendMessage(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                message
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onMessageSent(it)
                }, {
                    view.onError(it.message)
                }
            )
    }

    override fun attachView(view: ConversationContract.View) {
        this.view = view
        BlackduckApplication.appComponent.inject(this)

    }

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}