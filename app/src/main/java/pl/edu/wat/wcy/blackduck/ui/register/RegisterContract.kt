package pl.edu.wat.wcy.blackduck.ui.register

import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface RegisterContract {

    interface View : BaseContract.View {
        fun onRegisterSucces()
        fun onRegisterFailied(message: String?)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onUserRegisterClick(
            username: String,
            fullName: String,
            email: String,
            password: String
        )
    }

}