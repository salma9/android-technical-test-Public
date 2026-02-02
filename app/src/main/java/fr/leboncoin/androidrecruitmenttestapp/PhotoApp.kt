package fr.leboncoin.androidrecruitmenttestapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import javax.inject.Inject

@HiltAndroidApp
class PhotoApp : Application() {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate() {
        super.onCreate()
        analyticsHelper.initialize(this)
    }
}