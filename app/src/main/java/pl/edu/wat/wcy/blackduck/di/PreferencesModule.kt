package pl.edu.wat.wcy.blackduck.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager

@Module
object PreferencesModule {

    @Provides
    @JvmStatic
    @Reusable
    fun providePrefsManager(prefs: SharedPreferences) : PrefsManager =
        PrefsManager(prefs)

    @Provides
    @JvmStatic
    @Reusable
    fun provideSharedPreferences(context: Context) : SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}

