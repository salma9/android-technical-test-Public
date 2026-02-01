package fr.leboncoin.androidrecruitmenttestapp.ui.albumList

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.adevinta.spark.SparkTheme
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependenciesProvider
import fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail.DetailsActivity
import fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail.DetailsActivity.Companion.EXTRA_ALBUM_ID
import fr.leboncoin.androidrecruitmenttestapp.ui.common.ViewModelFactory
import fr.leboncoin.androidrecruitmenttestapp.ui.components.AlbumsScreen
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper

class MainActivity : ComponentActivity() {

    private val dependencies by lazy {
        (application as AppDependenciesProvider).dependencies
    }

    private val viewModel: AlbumsViewModel by lazy {
        val factory = ViewModelFactory(dependencies.dataDependencies.albumsRepository)
        ViewModelProvider(this, factory)[AlbumsViewModel::class.java]
    }

    private val analyticsHelper: AnalyticsHelper by lazy {
        dependencies.analyticsHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        analyticsHelper.initialize(this)

        setContent {
            SparkTheme {
                AlbumsScreen(
                    viewModel = viewModel,
                    onItemSelected = { album ->
                        analyticsHelper.trackSelection(album.id.toString())
                        val intent = Intent(this, DetailsActivity::class.java).apply {
                            putExtra(EXTRA_ALBUM_ID, album.id)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}