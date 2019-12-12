package pl.edu.wat.wcy.blackduck.ui.profile

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.preference.StoredUser
import pl.edu.wat.wcy.blackduck.data.request.LoginRequest
import pl.edu.wat.wcy.blackduck.data.responses.LoginResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import javax.inject.Inject

class ProfilePresenter: ProfileContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    lateinit var view: ProfileContract.View

    private var subscription: Disposable? = null

    override fun attachView(view: ProfileContract.View) {
        BlackduckApplication.appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {
        loadPosts()
        loadFollowers()
        loadFollowedUsers()
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun loadPosts(){
        subscription = portalApi
            .myPosts(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token
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

    fun loadFollowers(){
        subscription = portalApi
            .myFollowers(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onFollowersAvailable(it)
                }, {
                    view.onError(it.message)
                }
            )
    }

    fun loadFollowedUsers(){
        subscription = portalApi
            .myFollowedUsers(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onFollowedUsersAvailable(it)
                }, {
                    view.onError(it.message)
                }
            )
    }
}