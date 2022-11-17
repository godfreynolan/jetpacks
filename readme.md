# Room
## Setup
To add `room` to the project, the following dependencies were added to the `app` level `build.gradle`
```gradle
// Room
def room_version = "2.4.3"
implementation "androidx.room:room-runtime:$room_version"
annotationProcessor "androidx.room:room-compiler:$room_version"
kapt "androidx.room:room-compiler:$room_version"
```

## Entities
To use `room`, entities need to be described that model the table in the database. Therfore, following the schema of each table in the database, entities were created for every table.

In each `features` folder, a folder called `room` was created.
In the `features > company > room` folder, the `company` data class was transformed to a `room` entity.
```kotlin
@Entity(tableName = "agency")
data class Company(
  @PrimaryKey
  @ColumnInfo(name = "agency_id")
  val id: Int,
  @ColumnInfo(name = "agency_name")
  val name: String,
  @ColumnInfo(name = "agency_url")
  val url: String,
  @ColumnInfo(name = "agency_timezone")
  val timezone: String,
  @ColumnInfo(name = "agency_lang")
  val lang: String?,
  @ColumnInfo(name = "agency_phone")
  val phone: String?,
  @ColumnInfo(name = "agency_fare_url")
  val fare_url: String? = null,
  @ColumnInfo(name = "agency_email")
  val email: String? = null
)
```
In the `features > route > room` folder, the `route` data class was transformed to a `room` entity.
```kotlin
@Entity(tableName = "routes", primaryKeys = ["route_id", "agency_id"])
data class Route(
  @ColumnInfo(name = "route_id")
  val routeId: Int,
  @ColumnInfo(name = "agency_id")
  val companyId: Int,
  @ColumnInfo(name = "route_short_name")
  val routeShortName: String?,
  @ColumnInfo(name = "route_long_name")
  val routeLongName: String?,
  @ColumnInfo(name = "route_desc")
  val routeDesc: String?,
  @ColumnInfo(name = "route_type")
  val routeType: Int,
  @ColumnInfo(name = "route_url")
  val routeUrl: String?,
  @ColumnInfo(name = "route_color")
  val routeColor: String?,
  @ColumnInfo(name = "route_text_color")
  val routeTextColor: String?
)
```
In the `features > route > room` folder, the `Stop`, `StopTime`, and `Trip` data classes were transformed into `room` entities.

```kotlin
@Entity(
  tableName = "stops",
  primaryKeys = ["stop_id", "agency_id"]
)
data class Stop(
  @ColumnInfo(name = "stop_id")
  val stopId: Int,
  @ColumnInfo(name = "stop_code")
  val stopCode: String?,
  @ColumnInfo(name = "stop_name")
  val stopName: String?,
  @ColumnInfo(name = "stop_desc")
  val stopDesc: String?,
  @ColumnInfo(name = "stop_lat")
  val stopLat: String?,
  @ColumnInfo(name = "stop_lon")
  val stopLon: String?,
  @ColumnInfo(name = "zone_id")
  val zoneId: Int?,
  @ColumnInfo(name = "stop_url")
  val stopUrl: String?,
  @ColumnInfo(name = "location_type")
  val locationType: Int?,
  @ColumnInfo(name = "parent_station")
  val parentStation: Int?,
  @ColumnInfo(name = "stop_timezone")
  val stopTimezone: String?,
  @ColumnInfo(name = "wheelchair_boarding")
  val wheelChairBoarding: Int?,
  @ColumnInfo(name = "agency_id")
  val agencyId: Int
)
```
```kotlin
@Entity(
  tableName = "stop_times",
  primaryKeys = ["trip_id", "stop_id", "stop_sequence", "agency_id"],
  indices = [
      Index(value = arrayOf("trip_id"))
  ]
)
data class StopTime(
  @ColumnInfo(name = "trip_id")
  val tripId: Int,
  @ColumnInfo(name = "arrival_time")
  val arrivalTime: String?,
  @ColumnInfo(name = "departure_time")
  val departureTime: String?,
  @ColumnInfo(name = "stop_id")
  val stopId: Int,
  @ColumnInfo(name = "stop_sequence")
  val stopSequence: Int,
  @ColumnInfo(name = "stop_headsign")
  val stopHeadSign: String?,
  @ColumnInfo(name = "pickup_type")
  val pickUpType: Int?,
  @ColumnInfo(name = "drop_off_type")
  val dropOffType: Int?,
  @ColumnInfo(name = "shape_dist_traveled")
  val shapeDistTraveled: String?,
  @ColumnInfo(name = "timepoint")
  val timePoint: Int?,
  @ColumnInfo(name = "agency_id")
  val agencyId: Int
)
```
```kotlin
@Entity(
  tableName = "trips",
  primaryKeys = ["trip_id", "route_id", "direction_id", "shape_id", "agency_id"],
  indices = [
      Index(value = arrayOf("route_id"))
  ]
)
data class Trip(
  @ColumnInfo(name = "trip_id")
  val tripId: Int,
  @ColumnInfo(name = "route_id")
  val routeId: Int,
  @ColumnInfo(name = "service_id")
  val serviceId: Int,
  @ColumnInfo(name = "trip_headsign")
  val tripHeadSign: String?,
  @ColumnInfo(name = "trip_short_name")
  val tripShortName: String?,
  @ColumnInfo(name = "direction_id")
  val directionId: Int,
  @ColumnInfo(name = "block_id")
  val blockId: Int?,
  @ColumnInfo(name = "shape_id")
  val shapeId: String,
  @ColumnInfo(name = "wheelchair_accessible")
  val wheelchairAccessible: Int?,
  @ColumnInfo(name = "bikes_allowed")
  val bikesAllowed: Int?,
  @ColumnInfo(name = "agency_id")
  val agencyId: Int
)

```

## DAOs
`Room` DAOs perform the queries on the database. Therefore, each feature will contain a DAO. In the `features > company > room` folder, the interface `CompanyDAO` was created. In addition, a function was added that describes the query to retrieve a list of `company` objects.
```kotlin
@Dao
interface CompanyDAO {
    @Query("SELECT * FROM agency")
    fun getCompanies(): List<Company>
}
```

In the `features > route > room` folder, the interface `RouteDAO` was created. In addition, a function was added that describes the query to retrieve a list of `route` objects given a `companyId`.
```kotlin
@Dao
interface RouteDAO {
    @Query("SELECT * FROM routes WHERE agency_id = :companyId")
    fun getRoutes(companyId: Int): List<Route>
}
```

In the `features > stop > room` folder, the interface `StopDAO` was created. In addition, a function was added that describes the query to retrieve a list of `stop` objects given a `companyId` and `routeId`.
```kotlin
@Dao
interface StopDAO {
  @Query(
      "SELECT DISTINCT stops.* " +
        "  FROM trips " +
        "  INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id and stop_times.agency_id = trips.agency_id " +
        "  INNER JOIN stops ON stops.stop_id = stop_times.stop_id and stops.agency_id = stop_times.agency_id" +
        "  WHERE trips.route_id = :routeId and trips.agency_id = :companyId;",
  )
  fun getStopsForRoute(routeId: Int, companyId: Int): List<Stop>
}
```

## Repository
For each DAO, a `repository` was created to handle the the function calls. This is so the database object can be abstracted away from the `ViewModel`. These repositories will also be dependency injected into the view models.

In the `features > company > reposiotry` folder, an interface called `CompanyRepository` was created. In it, a function to get all companies was defined. Then, a class called `CompanyRepositoryImpl` was created and implemented the corresponding interface. In addition, the `CompanyDao` was injected into the implementation of the interface.

```kotlin
interface CompanyRepository {
    fun getCompanies(): List<Company>
}
```

```kotlin
class CompanyRepositoryImpl @Inject constructor(
    private val companyDAO: CompanyDAO
) : CompanyRepository {
    override fun getCompanies(): List<Company> {
        return companyDAO.getCompanies()
    }
}
```

In the `features > route > reposiotry` folder, an interface called `RouteRepository` was created. In it, a function to get all routes was defined. Then, a class called `RouteRepositoryImpl` was created and implemented the corresponding interface. In addition, the `RouteDao` was injected into the implementation of the interface.

```kotlin
interface RouteRepository {
    fun getRoutes(companyId: Int): List<Route>
}
```

```kotlin
class RouteRepositoryImpl @Inject constructor(
    private val routeDAO: RouteDAO
) : RouteRepository {

    override fun getRoutes(companyId: Int): List<Route> {
        return routeDAO.getRoutes(companyId)
    }
}
```

In the `features > stop > reposiotry` folder, an interface called `StopRepository` was created. In it, a function to get all stops was defined. Then, a class called `StopRepositoryImpl` was created and implemented the corresponding interface. In addition, the `StopDao` was injected into the implementation of the interface.

```kotlin
interface StopRepository {
  fun getStops(routeId: Int, companyId: Int): List<StopUi>
}
```

```kotlin
class StopRepositoryImpl @Inject constructor(
    private val stopDAO: StopDAO
): StopRepository {

    override fun getStops(routeId: Int, companyId: Int): List<StopUi> {
        val stops = stopDAO.getStopsForRoute(routeId, companyId)
        return stops.map { StopUi(it.stopId, it.stopName ?: it.stopId.toString()) }
    }
}
```

## Dependency Injection
Now, the `RoomDatabase` can be injected into the `DAO`s, the `DAO`s can be injected into the `repositories`, and the `repositories` can be injected into the `ViewModels`.

To instantiate the `RoomDatabase`, the following code was added to the `AppModule` class in the `di` package.
```kotlin
...

@Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
@Provides
fun provideJetpackEtaDatabase(
    @ApplicationContext appContext: Context
) = Room.databaseBuilder(appContext, AppDatabase::class.java, "jetpack.db")
    .createFromAsset("gtfs_room.db")
    .fallbackToDestructiveMigration()
    .build()

...
```

Next, the database can be injected into each of the `DAO`s
```kotlin
...

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

...
```

Then, the `DAO`s can be injected into the repositories
```kotlin
...

@Singleton
@Provides
fun provideCompanyRepository(companyDao: CompanyDAO): CompanyRepository = CompanyRepositoryImpl(companyDao)

@Singleton
@Provides
fun provideRouteRepository(routeDAO: RouteDAO): RouteRepository = RouteRepositoryImpl(routeDAO)

@Singleton
@Provides
fun provideStopRepository(stopDAO: StopDAO): StopRepository = StopRepositoryImpl(stopDAO)

...
```

All that is left is to replace the `SqliteHelper` injection in each view model with its corresponding repository
```kotlin
@HiltViewModel
class CompaniesViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
): ViewModel() {
  ...
}
```

```kotlin
@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val routeRepository: RouteRepository
): ViewModel() {
  ...
}
```

```kotlin
@HiltViewModel
class StopsViewModel @Inject constructor(
    private val stopRepository: StopRepository,
    private val encryptedSharedPrefs: EncryptedSharedPrefsHelper
): ViewModel() {
  ...
}
```