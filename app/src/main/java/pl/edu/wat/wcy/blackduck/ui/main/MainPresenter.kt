package pl.edu.wat.wcy.blackduck.ui.main

import android.content.SharedPreferences
import io.reactivex.disposables.Disposable
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import javax.inject.Inject

class MainPresenter: MainContract.Presenter{

    private val TAG = "MainPresenter"

    @Inject lateinit var portalApi: PortalApi

    @Inject lateinit var preferences: SharedPreferences

    private lateinit var view: MainContract.View

    private var subscription: Disposable? = null



    override fun attachView(view: MainContract.View) {
        BlackduckApplication.appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onViewDestroyed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
