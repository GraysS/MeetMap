package org.meetmap

import android.content.Context
import androidx.databinding.library.BuildConfig
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import timber.log.Timber

class MeetMapApplication : MultiDexApplication(){


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}