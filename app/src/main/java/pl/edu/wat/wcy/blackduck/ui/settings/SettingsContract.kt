package pl.edu.wat.wcy.blackduck.ui.settings

import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface SettingsContract {

    interface View: BaseContract.View

    interface Presenter: BaseContract.Presenter<View>
}