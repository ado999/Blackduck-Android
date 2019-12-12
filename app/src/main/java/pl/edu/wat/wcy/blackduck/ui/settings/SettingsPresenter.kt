package pl.edu.wat.wcy.blackduck.ui.settings

import pl.edu.wat.wcy.blackduck.BlackduckApplication

class SettingsPresenter: SettingsContract.Presenter {

    private lateinit var view: SettingsContract.View

    override fun attachView(view: SettingsContract.View) {
        (BlackduckApplication).appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onViewDestroyed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}