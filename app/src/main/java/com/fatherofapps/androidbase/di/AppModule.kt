package com.fatherofapps.androidbase.di

import android.content.Context
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }


//    @Provides
//    @Singleton
//    fun provideEncryptPreferenceManager(@ApplicationContext context: Context): EncryptPreferenceManager {
//        return EncryptPreferenceManager(context)
//    }

}