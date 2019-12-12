package pl.edu.wat.wcy.blackduck

import android.app.Application
import pl.edu.wat.wcy.blackduck.di.AppComponent
import pl.edu.wat.wcy.blackduck.di.DaggerAppComponent

class BlackduckApplication: Application() {

    private fun initDagger(application: BlackduckApplication) =
        DaggerAppComponent.builder()
            .provideContext(application)
            .build()

    override fun onCreate(){
        super.onCreate()
        appComponent = initDagger(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }

}