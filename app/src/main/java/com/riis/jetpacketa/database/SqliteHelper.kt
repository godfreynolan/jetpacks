package com.riis.jetpacketa.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.riis.jetpacketa.BuildConfig
import com.riis.jetpacketa.features.company.model.Company
import com.riis.jetpacketa.features.route.model.Route
import com.riis.jetpacketa.features.stop.model.StopUi
import java.io.*


class SqliteHelper(private val context: Context): SqliteHelperInterface {

    companion object {
        private const val TAG = "SqliteHelper"
        const val DB_NAME = "gtfs_room.db"
    }

    @SuppressLint("SdCardPath")
    private val dbPath: String = "/data/data/${BuildConfig.APPLICATION_ID}/databases/"
    private val outFileName: String = dbPath + DB_NAME
    private lateinit var db: SQLiteDatabase

    init {
        if (!checkDataBase()) copyDataBaseFromAssets()
    }

    //https://stackoverflow.com/questions/22627215/how-to-put-database-and-read-database-from-assets-folder-android-which-are-creat
    private fun copyDataBaseFromAssets() {
        try {
            val dbInputStream = context.assets.open(DB_NAME)
            val folder = File(dbPath)
            if (!folder.exists()) folder.mkdirs()

            val f = File(outFileName)
            if (f.exists())
                return

            val dbOutputStream = FileOutputStream(outFileName)

            //transfer bytes from the inputfile to the outputfile
            val buffer = ByteArray(1024)
            var length: Int = dbInputStream.read(buffer)

            while (length > 0) {
                dbOutputStream.write(buffer, 0, length)
                length = dbInputStream.read(buffer)
            }
            //Close the streams
            dbOutputStream.flush()
            dbOutputStream.close()
            dbInputStream.close()

            // Try and initialize again
            db = SQLiteDatabase.openDatabase(outFileName, null, 0)

        } catch (e: Exception) {
            Log.e(TAG, "copyDataBaseFromAssets: Couldn't move database from assets.")
        }

    }

    private fun checkDataBase(): Boolean {
        var flag = false
        try {
            db = SQLiteDatabase.openDatabase(outFileName, null, 0)
            flag = true
        } catch (e: SQLiteCantOpenDatabaseException) {
            Log.i(TAG, "checkDataBase: Database does not exist")
        }
        return flag
    }

    override fun getCompanies(): List<Company> {
        val newCompanies = mutableListOf<Company>()
        val query = db.rawQuery("SELECT * FROM agency", null)
        if(query.count > 0) {
            query.moveToFirst()
            do {
                // Get the column indexes from the query
                val agencyIdColumnIndex = query.getColumnIndex("agency_id")
                val agencyNameColumnIndex = query.getColumnIndex("agency_name")
                val agencyUrlColumnIndex = query.getColumnIndex("agency_url")
                val agencyTimezoneColumnIndex = query.getColumnIndex("agency_timezone")
                val agencyLangColumnIndex = query.getColumnIndex("agency_lang")
                val agencyPhoneColumnIndex = query.getColumnIndex("agency_phone")
                val agencyFareUrlColumnIndex = query.getColumnIndex("agency_fare_url")
                val agencyEmailColumnIndex = query.getColumnIndex("agency_email")

                // Check if any the columns do not exist
                if (
                    agencyIdColumnIndex == -1 ||
                    agencyNameColumnIndex == -1 ||
                    agencyUrlColumnIndex == -1 ||
                    agencyTimezoneColumnIndex == -1 ||
                    agencyLangColumnIndex == -1 ||
                    agencyPhoneColumnIndex == -1 ||
                    agencyFareUrlColumnIndex == -1 ||
                    agencyEmailColumnIndex == -1
                ) continue

                // Get the values from the record
                val agencyId = query.getInt(agencyIdColumnIndex)
                val agencyName = query.getString(agencyNameColumnIndex)
                val agencyUrl = query.getString(agencyUrlColumnIndex)
                val agencyTimezone = query.getString(agencyTimezoneColumnIndex)
                val agencyLang = query.getString(agencyLangColumnIndex)
                val agencyPhone = query.getString(agencyPhoneColumnIndex)
                val agencyFareUrl = query.getString(agencyFareUrlColumnIndex)
                val agencyEmail = query.getString(agencyEmailColumnIndex)

                // Create the `Company` Object
                newCompanies.add(
                    Company(agencyId, agencyName, agencyUrl, agencyTimezone, agencyLang, agencyPhone, agencyFareUrl, agencyEmail)
                )

            } while (query.moveToNext())
        }
        query.close()
        return newCompanies
    }

    override fun getRoutes(companyId: Int): List<Route> {
        val newRoutes = mutableListOf<Route>()
        val query = db.rawQuery("SELECT * FROM routes WHERE agency_id = ?", arrayOf(companyId.toString()))
        if(query.count > 0) {
            query.moveToFirst()
            do {
                // Get the column indexes from the query
                val routeIdColumnIndex = query.getColumnIndex("route_id")
                val agencyIdColumnIndex = query.getColumnIndex("agency_id")
                val routeShortNameColumnIndex = query.getColumnIndex("route_short_name")
                val routeLongNameColumnIndex = query.getColumnIndex("route_long_name")
                val routeDescColumnIndex = query.getColumnIndex("route_desc")
                val routeTypeColumnIndex = query.getColumnIndex("route_type")
                val routeUrlColumnIndex = query.getColumnIndex("route_url")
                val routeColorColumnIndex = query.getColumnIndex("route_color")
                val routeTextColorColumnIndex = query.getColumnIndex("route_text_color")

                // Check if any the columns do not exist
                if (
                    routeIdColumnIndex == -1 ||
                    agencyIdColumnIndex == -1 ||
                    routeShortNameColumnIndex == -1 ||
                    routeLongNameColumnIndex == -1 ||
                    routeDescColumnIndex == -1 ||
                    routeTypeColumnIndex == -1 ||
                    routeUrlColumnIndex == -1 ||
                    routeColorColumnIndex == -1 ||
                    routeTextColorColumnIndex == -1
                ) continue

                // Create the `Route` Object
                newRoutes.add(
                    Route(
                        query.getInt(routeIdColumnIndex),
                        query.getInt(agencyIdColumnIndex),
                        query.getInt(routeShortNameColumnIndex),
                        query.getString(routeLongNameColumnIndex),
                        query.getString(routeDescColumnIndex),
                        query.getInt(routeTypeColumnIndex),
                        query.getString(routeUrlColumnIndex),
                        query.getString(routeColorColumnIndex),
                        query.getString(routeTextColorColumnIndex)
                    )
                )

            } while (query.moveToNext())
        }

        query.close()
        return newRoutes
    }

    override fun getStopsForRoute(routeId: Int, companyId: Int): List<StopUi> {
        val stops = mutableListOf<StopUi>()

        val query = db.rawQuery(
            "SELECT DISTINCT stops.* " +
                    "  FROM trips " +
                    "  INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id and stop_times.agency_id = trips.agency_id " +
                    "  INNER JOIN stops ON stops.stop_id = stop_times.stop_id and stops.agency_id = stop_times.agency_id" +
                    "  WHERE trips.route_id = ? and trips.agency_id = ?;",
            listOf<String>(routeId.toString(), companyId.toString()).toTypedArray()
        )

        if(query.count > 0) {
            query.moveToFirst()
            do {
                // Get the values from the record
                val stopIdCA = query.getColumnIndex("stop_id")
                val stopNameCA = query.getColumnIndex("stop_name")
                if(stopIdCA == -1 || stopNameCA == -1) return emptyList()
                
                val stopId = query.getInt(stopIdCA)
                val stopName = query.getString(stopNameCA)

                stops.add(StopUi(stopId, stopName))

            } while (query.moveToNext())
        }
        query.close()
        return stops
    }
}