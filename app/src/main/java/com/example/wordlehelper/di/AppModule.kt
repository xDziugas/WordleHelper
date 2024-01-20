package com.example.wordlehelper.di

import android.content.Context
import com.example.wordlehelper.WordleHelperApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideContext(application: WordleHelperApp): Context = application.applicationContext

}