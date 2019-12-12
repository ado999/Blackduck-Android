package pl.edu.wat.wcy.blackduck.ui.main

import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {

    }

    interface Presenter : BaseContract.Presenter<View> {

    }
}