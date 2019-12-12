package pl.edu.wat.wcy.blackduck.ui.foreignProfile

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import javax.inject.Inject

class ForeignProfilePresenter: ForeignProfileContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    lateinit var view: ForeignProfileContract.View

    private var subscription: Disposable? = null

    override fun fetchUser(username: String) {
        subscription = portalApi.getProfile(
            Constants.APPLICATION_JSON,
            Constants.APPLICATION_JSON,
            prefs.loadString(Key.TOKEN),
            username
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onProfileReady(it)
                },
                {
                    view.onError(it.message)
                }
            )
    }

    override fun fetchPosts(username: String) {
        subscription = portalApi
            .getForeignPosts(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token,
                username
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onPostsAvailable(it)
                }, {
                    view.onError(it.message)
                }
            )
    }

    override fun setFollow(displayName: String, checked: Boolean) {
        subscription = portalApi
            .setFollow(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token,
                displayName,
                checked
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.e("FOLLOW", it.toString())
            }
    }

    override fun attachView(view: ForeignProfileContract.View) {
        this.view = view
        BlackduckApplication.appComponent.inject(this)
    }

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}