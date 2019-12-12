package pl.edu.wat.wcy.blackduck.ui.login

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.preference.StoredUser
import pl.edu.wat.wcy.blackduck.data.request.LoginRequest
import pl.edu.wat.wcy.blackduck.data.responses.LoginResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserShortResponse
import javax.inject.Inject

class LoginPresenter : LoginContract.Presenter {

    val TAG = "LoginPresenter"

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefsMgr: PrefsManager

    private lateinit var view: LoginContract.View

    private var subscription: Disposable? = null

    override fun requestLogin(username: String, password: String) {
        subscription = portalApi
            .login(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                LoginRequest(username, password)
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response: LoginResponse? ->
                    response.let {
                        prefsMgr.storeUser(
                            StoredUser(
                                it?.token!!,
                                it.user?.uuid!!,
                                it.user.displayName!!,
                                it.user.fullName!!,
                                it.user.creationDate.toString(),
                                it.user.profileThumbnail!!,
                                it.user.profileBackgroundUrl!!,
                                it.user.description ?: "",
                                it.user.email!!
                            )
                        )
                    }
                    view.onLoginSuccess()
                },
                { t: Throwable ->
                    view.onLoginFailied(t.message)
                }
            )
    }

    override fun attachView(view: LoginContract.View) {
        (BlackduckApplication).appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}