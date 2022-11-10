package com.riis.jetpacketa.di

import android.content.Context
import androidx.room.Room
import com.riis.jetpacketa.AppDatabase
import com.riis.jetpacketa.features.company.repository.CompanyRepository
import com.riis.jetpacketa.features.company.repository.CompanyRepositoryImpl
import com.riis.jetpacketa.features.company.room.CompanyDAO
import com.riis.jetpacketa.features.route.repository.RouteRepository
import com.riis.jetpacketa.features.route.repository.RouteRepositoryImpl
import com.riis.jetpacketa.features.route.room.RouteDAO
import com.riis.jetpacketa.features.stop.repository.StopRepository
import com.riis.jetpacketa.features.stop.repository.StopRepositoryImpl
import com.riis.jetpacketa.features.stop.room.StopDAO
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

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideJetpackEtaDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(appContext, AppDatabase::class.java, "jetpack.db")
        .createFromAsset("gtfs_room.db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    // Provide the `CompanyDAO` to be injected into the `Company` repository
    fun provideCompanyDao(db: AppDatabase) = db.companyDao()

    @Singleton
    @Provides
    // Provide the `RouteDAO` to be injected into the `Route` repository
    fun provideRouteDao(db: AppDatabase) = db.routeDao()

    @Singleton
    @Provides
    // Provide the `StopDAO` to be injected into the `Stop` repository
    fun provideStopDao(db: AppDatabase) = db.stopDao()

    @Singleton
    @Provides
    fun provideEncryptedSharedPrefsHelper(@ApplicationContext appContext: Context): EncryptedSharedPrefsHelper = EncryptedSharedPrefsHelperImpl(appContext)


    @Singleton
    @Provides
    fun provideCompanyRepository(companyDao: CompanyDAO): CompanyRepository = CompanyRepositoryImpl(companyDao)

    @Singleton
    @Provides
    fun provideRouteRepository(routeDAO: RouteDAO): RouteRepository = RouteRepositoryImpl(routeDAO)

    @Singleton
    @Provides
    fun provideStopRepository(stopDAO: StopDAO): StopRepository = StopRepositoryImpl(stopDAO)
}