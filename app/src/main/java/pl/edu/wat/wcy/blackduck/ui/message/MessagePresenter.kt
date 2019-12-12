package pl.edu.wat.wcy.blackduck.ui.message

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import javax.inject.Inject

class MessagePresenter : MessageContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    private lateinit var view: MessageContract.View

    var subscription: Disposable? = null

    override fun attachView(view: MessageContract.View) {
        this.view = view
        BlackduckApplication.appComponent.inject(this)
    }

    override fun onConversationsRefresh() {
        subscription = portalApi
            .getConversations(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN)
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onConversationsAvailable(it)
                },
                {
                    view.onError(it.message)
                }
            )
    }

    override fun onViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}