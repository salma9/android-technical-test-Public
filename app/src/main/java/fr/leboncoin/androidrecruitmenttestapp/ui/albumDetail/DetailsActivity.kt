package fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.image.Illustration
import fr.leboncoin.androidrecruitmenttestapp.R
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependenciesProvider
import fr.leboncoin.androidrecruitmenttestapp.ui.components.DetailsScreen
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper

class DetailsActivity : ComponentActivity() {

    private val dependencies by lazy {
        (application as AppDependenciesProvider).dependencies
    }

    private val albumId: Int by lazy {
        intent.getIntExtra(EXTRA_ALBUM_ID, -1)
    }

    private val viewModel: DetailsViewModel by lazy {
        val factory = DetailsViewModel.Factory(albumId, dependencies.dataDependencies.albumsRepository)
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