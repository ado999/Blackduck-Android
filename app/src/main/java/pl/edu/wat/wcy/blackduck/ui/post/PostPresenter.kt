package pl.edu.wat.wcy.blackduck.ui.post

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.CommentRequest
import pl.edu.wat.wcy.blackduck.data.request.RateRequest
import javax.inject.Inject

class PostPresenter : PostContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefsManager: PrefsManager

    lateinit var view: PostContract.View

    private var subscription: Disposable? = null

    override fun fetchPost(postId: Int) {
        subscription = portalApi
            .getPost(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefsManager.loadUser().token,
                postId
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onPostAvailable(it)
                }, {
                    view.onError(it.message)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun rate(rate: Int, postId: Int) {
        portalApi.rate(
            Constants.APPLICATION_JSON,
            Constants.APPLICATION_JSON,
            prefsManager.loadString(Key.TOKEN),
            RateRequest(rate, postId)
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onRateSuccess(rate, it)
                },
                {
                    view.onError(it.message)
                }
            )
    }

    override fun addComment(text: String, id: Int, videoTime: Long) {
        subscription = portalApi.comment(
            Constants.APPLICATION_JSON,
            Constants.APPLICATION_JSON,
            prefsManager.loadString(Key.TOKEN),
            CommentRequest(id, text, videoTime)
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onCommentAdded()
                },
                {
                    view.onError(it.message)
                }
            )
    }


    override fun attachView(view: PostContract.View) {
        this.view = view
        BlackduckApplication.appComponent.inject(this)
    }

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}