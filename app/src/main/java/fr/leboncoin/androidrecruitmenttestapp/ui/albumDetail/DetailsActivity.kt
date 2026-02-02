package fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.ui.theme.AppTheme
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : ComponentActivity() {

    private val albumId: Int by lazy {
        intent.getIntExtra(EXTRA_ALBUM_ID, -1)
    }

    private val viewModel: DetailsViewModel by viewModels()

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analyticsHelper.initialize(this)
        analyticsHelper.trackScreenView("Details_$albumId")

        setContent {
            AppTheme {
                DetailsScreen(viewModel = viewModel, onBack = { finish() })
            }
        }
    }

    companion object {
        const val EXTRA_ALBUM_ID = "ALBUM_ID"
    }
}