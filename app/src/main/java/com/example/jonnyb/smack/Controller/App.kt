package com.example.jonnyb.smack.Controller

import android.app.Application
import com.example.jonnyb.smack.Utilities.SharedPrefs

/**
 * Created by jonnyb on 9/8/17.
 */
class App : Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}