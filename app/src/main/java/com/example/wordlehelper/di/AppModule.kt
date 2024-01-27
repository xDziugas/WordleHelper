package com.example.wordlehelper.di

import android.content.Context
import com.example.wordlehelper.WordleHelperApp
import com.example.wordlehelper.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideContext(application: WordleHelperApp): Context = application.applicationContext

    @Provides
    fun provideWordRepository(@ApplicationContext context: Context): WordRepository {
        return WordRepository(context)
    }
}