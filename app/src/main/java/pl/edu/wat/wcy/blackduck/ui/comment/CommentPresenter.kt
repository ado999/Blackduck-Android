package pl.edu.wat.wcy.blackduck.ui.comment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.CommentRequest
import javax.inject.Inject

class CommentPresenter : CommentContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    lateinit var view: CommentContract.View

    var subscription: Disposable? = null

    override fun getPost(postId: Int) {
        subscription = portalApi
            .getPost(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                postId
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onPostAvailable(it)
                },
                {
                    view.onError(it.localizedMessage)
                }
            )
    }

    override fun putComment(postId: Int, content: String) {
        subscription = portalApi
            .comment(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                CommentRequest(postId, content, 0L)
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    it
                    view.onPutCommentResponseSuccess(it)
                }, {
                    it
                        view.onError("Wystąpił błąd. Sprawdź połączenie z internetem")
                }
            )

    }

    override fun attachView(view: CommentContract.View) {
        BlackduckApplication.appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {

    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}