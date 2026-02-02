package fr.leboncoin.androidrecruitmenttestapp.ui.albumList

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail.DetailsActivity
import fr.leboncoin.androidrecruitmenttestapp.ui.albumDetail.DetailsActivity.Companion.EXTRA_ALBUM_ID
import fr.leboncoin.androidrecruitmenttestapp.ui.theme.AppTheme
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AlbumsViewModel by viewModels()

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
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