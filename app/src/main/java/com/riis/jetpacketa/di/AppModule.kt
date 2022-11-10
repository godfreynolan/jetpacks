package com.riis.jetpacketa.di

import android.content.Context
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.database.SqliteHelperInterface
import com.riis.jetpacketa.security.EncryptedSharedPrefsHelper
import com.riis.jetpacketa.security.EncryptedSharedPrefsHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// App Module to handle passing only one instance of `SqliteHelper`
// to all ViewModels that inject them
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // Function that will provide the instantiated instance of `SqliteHelper`
    @Singleton
    @Provides
    fun provideSqliteHelper(@ApplicationContext appContext: Context): SqliteHelperInterface = SqliteHelper(appContext)

    @Singleton
    @Provides
    fun provideEncryptedSharedPrefsHelper(@ApplicationContext appContext: Context): EncryptedSharedPrefsHelper = EncryptedSharedPrefsHelperImpl(appContext)
}