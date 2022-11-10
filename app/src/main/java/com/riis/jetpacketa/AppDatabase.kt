package com.riis.jetpacketa

import androidx.room.Database
import androidx.room.RoomDatabase
import com.riis.jetpacketa.features.company.room.Company
import com.riis.jetpacketa.features.company.room.CompanyDAO
import com.riis.jetpacketa.features.route.room.Route
import com.riis.jetpacketa.features.route.room.RouteDAO
import com.riis.jetpacketa.features.stop.room.Stop
import com.riis.jetpacketa.features.stop.room.StopDAO
import com.riis.jetpacketa.features.stop.room.StopTime
import com.riis.jetpacketa.features.stop.room.Trip

@Database(entities = [Company::class, Route::class, Stop::class, Trip::class, StopTime::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDAO
    abstract fun routeDao(): RouteDAO
    abstract fun stopDao(): StopDAO
}