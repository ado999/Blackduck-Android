package pl.edu.wat.wcy.blackduck.ui.login

import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface LoginContract {

    interface View : BaseContract.View {
        fun onLoginSuccess()
        fun onLoginFailied(msg: String?)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun requestLogin(username: String, password: String)
    }
}