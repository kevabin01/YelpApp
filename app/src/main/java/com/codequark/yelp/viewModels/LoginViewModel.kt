package com.codequark.yelp.viewModels

import android.app.Application
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.codequark.yelp.R
import com.codequark.yelp.models.login.Login
import com.codequark.yelp.utils.Constants.LoginStateDef

open class LoginViewModel(application: Application): RequestViewModel(application) {
    @NonNull
    private val loginState: LiveData<Int> = repository.getLoginState()

    var email = ""
    var password = ""

    @NonNull
    fun getLoginState(): LiveData<Int> {
        return loginState
    }

    fun setLoginState(@LoginStateDef state: Int) {
        repository.setLoginState(state)
    }

    fun login(@NonNull view: View) {
        email = email.trim()

        if(email.isEmpty()) {
            setLoginState(LoginStateDef.STATE_LOGIN_ERROR_EMAIL_EMPTY)
            return
        }

        if(password.isEmpty()) {
            setLoginState(LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_EMPTY)
            return
        }

        if(password.length < 6) {
            setLoginState(LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_LENGTH)
            return
        }

        val login = Login(email, password)
        repository.login(login)
    }

    fun logout() {
        repository.logout()
    }

    fun registrarse(@NonNull view: View) {
        this.repository.setDestination(R.id.navigationRegister)
    }

    fun register(@NonNull view: View) {
        email = email.trim()

        if(email.isEmpty()) {
            setLoginState(LoginStateDef.STATE_LOGIN_ERROR_EMAIL_EMPTY)
            return
        }

        if(password.isEmpty()) {
            setLoginState(LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_EMPTY)
            return
        }

        if(password.length < 6) {
            setLoginState(LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_LENGTH)
            return
        }

        val login = Login(email, password)
        repository.register(login)
    }
}