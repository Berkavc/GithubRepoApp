package com.github.repos

import android.app.Application

class MainApp : Application() {

    private var userPhoneCountryCode: String = "+90"

    fun getUserPhoneCountryCode(): String {
        return userPhoneCountryCode
    }

    companion object {
        private var mainApp: MainApp? = null

        @JvmStatic
        @Synchronized
        fun getMainApp(): MainApp? {
            if (mainApp == null) {
                mainApp = MainApp()
            }
            return mainApp
        }

        fun setMainApp(mainApp: MainApp?) {
            MainApp.Companion.mainApp = mainApp
        }
    }

}