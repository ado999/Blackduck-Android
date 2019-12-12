package pl.edu.wat.wcy.blackduck.ui.home

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import java.net.ConnectException
import java.net.NoRouteToHostException
import javax.inject.Inject

class HomePresenter : HomeContract.Presenter {

    val TAG = "HomePresenter"

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    private var subscription: Disposable? = null

    private lateinit var view: HomeContract.View

    private var page = 0

    override fun attachView(view: HomeContract.View) {
        BlackduckApplication.appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }

    override fun refreshPosts() {
        page = 0
        fetchPosts()
    }

    override fun onBottomAchieved() {
        subscription =
            portalApi.getPosts(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                page++
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.isNotEmpty()){
                        view.onPostsAdded(it)
                    }
                }, {
                    when (it){
                        is NoRouteToHostException -> view.onError("Sprawdź połączenie z internetem")
                        is ConnectException -> view.onError("Brak połączenia z internetem")
                        else -> view.onError("Coś poszło nie tak")
                    }
                })
    }

    override fun fetchPosts() {
        subscription =
            portalApi.getPosts(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                page++
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.isNotEmpty()){
                        view.onPostsAvailable(it)
                    } else {
                        view.onNoPostsAvailable()
                    }
                }, {
                    when (it){
                        is NoRouteToHostException -> view.onError("Sprawdź połączenie z internetem")
                        is ConnectException -> view.onError("Brak połączenia z internetem")
                        else -> view.onError("Coś poszło nie tak")
                    }
                })
    }

}