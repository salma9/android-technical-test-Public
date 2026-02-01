package fr.leboncoin.androidrecruitmenttestapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors
import com.adevinta.spark.tokens.sparkTypography

/**
 * Custom App Theme that wraps SparkTheme.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkSparkColors(
            main = LeboncoinOrange,
            support = LeboncoinBlue,
            error = ErrorRed,
            surface = DarkSurface,
            background = DarkBackground
        )
    } else {
        lightSparkColors(
            main = LeboncoinOrange,
            support = LeboncoinBlue,
            error = ErrorRed,
            surface = LightSurface,
            onSurface = Black
        )
    }

    val sparkTypography = sparkTypography(
        display1 = AppTypography.displayLarge,
        headline1 = AppTypography.headlineMedium,
        body1 = AppTypography.bodyLarge,
        body2 = AppTypography.bodyMedium,
    )

    SparkTheme(
        colors = colors,
        typography = sparkTypography,
        content = content
    )
}
