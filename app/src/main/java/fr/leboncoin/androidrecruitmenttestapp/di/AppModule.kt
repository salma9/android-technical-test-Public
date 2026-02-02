package fr.leboncoin.androidrecruitmenttestapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import java.util.logging.Logger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAnalyticsHelper(): AnalyticsHelper = AnalyticsHelper()

    @Provides
    @Singleton
    fun provideLogger(): Logger = Logger.getGlobal()
}