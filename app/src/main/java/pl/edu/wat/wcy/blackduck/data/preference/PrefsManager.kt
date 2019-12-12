package pl.edu.wat.wcy.blackduck.data.preference

import android.annotation.SuppressLint
import android.content.SharedPreferences

class PrefsManager(val prefs: SharedPreferences) {

    fun storeUser(user: StoredUser) {
        prefs.edit()
            .putString(Key.TOKEN.name, user.token)
            .putString(Key.UUID.name, user.uuid)
            .putString(Key.DISPLAY_NAME.name, user.displayName)
            .putString(Key.FULL_NAME.name, user.fullname)
            .putString(Key.CREATION_DATE.name, user.creationDate)
            .putString(Key.PHOTO_URL.name, user.profilePhotoUrl)
            .putString(Key.BACKGROUND_URL.name, user.profileBackgroundUrl)
            .putString(Key.DESCRIPTION.name, user.description)
            .putString(Key.EMAIL.name, user.email)
            .apply()
    }

    fun loadUser(): StoredUser {
        return StoredUser(
            token = loadString(Key.TOKEN),
            uuid = loadString(Key.UUID),
            displayName = loadString(Key.DISPLAY_NAME),
            fullname = loadString(Key.FULL_NAME),
            creationDate = loadString(Key.CREATION_DATE),
            profilePhotoUrl = loadString(Key.PHOTO_URL),
            profileBackgroundUrl = loadString(Key.BACKGROUND_URL),
            description = loadString(Key.DESCRIPTION),
            email = loadString(Key.EMAIL)
        )
    }

    fun eraseUser() {
        prefs.edit()
            .remove(Key.TOKEN.name)
            .remove(Key.UUID.name)
            .remove(Key.DISPLAY_NAME.name)
            .remove(Key.FULL_NAME.name)
            .remove(Key.CREATION_DATE.name)
            .remove(Key.PHOTO_URL.name)
            .remove(Key.BACKGROUND_URL.name)
            .remove(Key.DESCRIPTION.name)
            .remove(Key.EMAIL.name)
            .apply()
    }

    fun loadString(key: Key) = prefs.getString(key.name, "")

    fun loadInt(key: Key) = prefs.getInt(key.name, -1)

    fun loadBoolean(key: Key) = prefs.getBoolean(key.name, false)

    @SuppressLint("ApplySharedPref")
    private fun storeString(key: Key, str: String) {
        prefs.edit()
            .putString(key.name, str)
            .commit()
    }

    private fun storeInt(key: Key, int: Int) {
        prefs.edit()
            .putInt(key.name, int)
            .apply()
    }

    private fun storeBoolean(key: Key, b: Boolean) {
        prefs.edit()
            .putBoolean(key.name, b)
            .apply()
    }

}