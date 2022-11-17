# Hilt
## Setup
To add `hilt` to the project, a few dependencies and settings must be added to the `app` level `build.gradle` and `project` level `build.gradle`

In the `app` level `build.gradle` the following code was added
```gradle
plugins {
  ...
  
  id 'kotlin-kapt'
  id 'com.google.dagger.hilt.android'
}

android {
  ...

  kapt {
      correctErrorTypes true
  }
}

dependencies {
    ...

    // Hilt
    implementation "com.google.dagger:hilt-android:2.44"
    kapt "com.google.dagger:hilt-compiler:2.44"

    ...
}
```

Next, in the `project` level `build.gradle`, add the following plugin
```gradle
plugins {
  ...
  
  id 'com.google.dagger.hilt.android' version '2.44' apply false
}
```

## Application
For `hilt` to function, the `appplication` class must be overridden to identify the app as able to use `hilt`. A file called `MainApplication` was created and the annotation `HiltAndroidApp` was added to the class.
```kotlin
// Define this application as a `HiltAndroidApplication`
@HiltAndroidApp
class MainApplication: Application() {}
```

Next, the `AndroidManifest.xml` must be updated to point to the new application file.
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:name=".MainApplication"
        ...
        >

    </application>

</manifest>
```

## Interfaces
To dependency inject a class, an `interface` describing all the methods to inject must be created. Therefore, the `EncryptedSharedPrefsHelper` and `SqliteHelper` had interfaces created for them. I will only describe the `SqliteHelper`.

In the `database` package, a file called `SqliteHelperInterface` was created and the following methods defined
```kotlin
interface SqliteHelperInterface {
    fun getCompanies(): List<Company>
    fun getRoutes(companyId: Int): List<Route>
    fun getStopsForRoute(routeId: Int, companyId: Int): List<StopUi>
}
```

Next, the `SqliteHelper` class implemented the interface and overrode the required functions.

## App Module
To provide the dependency injection, a package called `di` was created. Inside this package, a class called `AppModule` was created.
```kotlin
// App Module to handle passing only one instance of `SqliteHelper`
// to all ViewModels that inject them
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

}
```

Two functions were created in this class to provide the `SqliteHelper` and `EncryptedSharedPrefsHelper`. Both of these functions were annotated as `Singleton` and `Provides`. In addition, since these classes need context, `ApplicationContext` was provided as an argument.

```kotlin
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
```

## View Model
The classes can now be injected directly into the `ViewModels`. The process for injecting the `SqliteHelper` and `EncryptedSharedPrefsHelper` will be described for the `StopsViewModel`, but the process is the same for the other view models.

In the `StopsViewModel` the class was changed to extend `ViewModel` instead of `AndroidViewModel` as `context` is no longer required inside the class. The class declaration was changed to
```kotlin
@HiltViewModel
class StopsViewModel @Inject constructor(
    private val helper: SqliteHelperInterface,
    private val encryptedSharedPrefs: EncryptedSharedPrefsHelper
): ViewModel() {

  ...
}
```
> Note: The previous instantiations of `helper` and `encryptedSharedPrefs` were removed.

## Entry Points
For `hilt` to successfully inject these classes, any activity or fragment that interacts with the view models. Therefore, `MainActivity` and subsequent fragments were annotated with `AndroidEntryPoint`
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  ...
}
```

```kotlin
@AndroidEntryPoint
class StopsFragment: Fragment() {
  ...
}
```

```kotlin
@AndroidEntryPoint
class CompaniesFragment: Fragment() {
  ...
}
```

```kotlin
@AndroidEntryPoint
class RouteFragment: Fragment() {
  ...
}
```