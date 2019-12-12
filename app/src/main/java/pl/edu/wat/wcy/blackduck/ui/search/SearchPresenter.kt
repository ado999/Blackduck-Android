package pl.edu.wat.wcy.blackduck.ui.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import javax.inject.Inject

class SearchPresenter : SearchContract.Presenter {

    @Inject
    lateinit var prefs: PrefsManager

    @Inject
    lateinit var portalApi: PortalApi

    lateinit var view: SearchContract.View

    private var subscription: Disposable? = null

    override fun searchInPosts(text: String) {
        subscription =
            portalApi.searchPosts(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                text
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view.onPostsSearched(it)
                }, {
                    view.onSearchError(it.message!!)
                })
    }

    override fun searchInUsers(text: String) {
        subscription =
            portalApi.searchUsers(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                text
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view.onUsersSearched(it)
                }, {
                    view.onSearchError(it.message!!)
                })
    }

    override fun attachView(view: SearchContract.View) {
        BlackduckApplication.appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}