package fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.adevinta.spark.SparkTheme
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependenciesProvider
import fr.leboncoin.androidrecruitmenttestapp.ui.common.ViewModelFactory
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper

class DetailsActivity : ComponentActivity() {

    private val dependencies by lazy {
        (application as AppDependenciesProvider).dependencies
    }

    private val albumId: Int by lazy {
        intent.getIntExtra(EXTRA_ALBUM_ID, -1)
    }

    private val viewModel: DetailsViewModel by lazy {
        val factory = ViewModelFactory(dependencies.dataDependencies.albumsRepository, albumId)
        ViewModelProvider(this, factory)[DetailsViewModel::class.java]
    }

    private val analyticsHelper: AnalyticsHelper by lazy {
        dependencies.analyticsHelper
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analyticsHelper.initialize(this)
        analyticsHelper.trackScreenView("Details_$albumId")

        setContent {
            SparkTheme {
                DetailsScreen(viewModel = viewModel, onBack = { finish() })
            }
        }
    }

    companion object {
        const val EXTRA_ALBUM_ID = "ALBUM_ID"
    }
}