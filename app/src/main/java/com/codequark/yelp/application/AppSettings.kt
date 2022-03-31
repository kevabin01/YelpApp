package com.codequark.yelp.application

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.codequark.yelp.R
import com.codequark.yelp.managers.PreferenceManager
import com.codequark.yelp.models.login.Login

class AppSettings {
    companion object {
        fun login(@NonNull login: Login) {
            setLogin(login)
        }

        fun logout() {
            PreferenceManager.getInstance().cleanKey(R.string.key_firebase_id)
            PreferenceManager.getInstance().cleanKey(R.string.key_email)
            PreferenceManager.getInstance().cleanKey(R.string.key_password)
        }

        private fun setLogin(@NonNull login: Login) {
            PreferenceManager.getInstance().set(R.string.key_firebase_id, login.firebaseId)
            PreferenceManager.getInstance().set(R.string.key_email, login.email)
            PreferenceManager.getInstance().set(R.string.key_password, login.password)
        }

        @Nullable
        fun getLogin(): Login? {
            val firebaseId = PreferenceManager.getInstance().getString(R.string.key_firebase_id)
            val email = PreferenceManager.getInstance().getString(R.string.key_email)
            val password = PreferenceManager.getInstance().getString(R.string.key_password)

            return if(firebaseId.isEmpty() || email.isEmpty() || password.isEmpty()) {
                null
            } else {
                Login(firebaseId, email, password)
            }
        }
    }
}