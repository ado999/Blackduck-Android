package pl.edu.wat.wcy.blackduck.ui.register

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.request.SignupRequest
import javax.inject.Inject

class RegisterPresenter : RegisterContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    private lateinit var view: RegisterContract.View

    private var subscription: Disposable? = null

    override fun onUserRegisterClick(
        username: String,
        fullName: String,
        email: String,
        password: String
    ) {
        subscription =
            portalApi.signup(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                SignupRequest(
                    username = username,
                    fullName = fullName,
                    email = email,
                    password = password
                )
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    if(it.success){
                        view.onRegisterSucces()
                    } else {
                        view.onRegisterFailied(it.message)
                    }
                }

    }

    override fun attachView(view: RegisterContract.View) {
        this.view = view
        BlackduckApplication.appComponent.inject(this)
    }

    override fun onViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}