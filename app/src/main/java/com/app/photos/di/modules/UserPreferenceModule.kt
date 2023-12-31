package com.app.photos.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.app.photos.data.UserPreferences
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserPreferenceModule {

    @Singleton
    @Provides
    fun provideUserPreference(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}