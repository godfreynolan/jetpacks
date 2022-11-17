# Navigation
## Setup
To add `navigation` to this project, the following dependencies were added to the `app` level `build.gradle`
```gradle
  // Navigation
  def navigation_version = "2.5.3"
  implementation "androidx.navigation:navigation-fragment-ktx:$navigation_version"
  implementation "androidx.navigation:navigation-ui-ktx:$navigation_version"
```

## NavGraph
To use navigation in fragments, they must first be defined in a NavGraph. This will also contain the different actions that each fragment can perfom. For example, the `CompaniesFragment` will have a defined action for displaying the `StopsFragment`

In the `res` folder, and new folder called `navigation` was created. In this folder, the file `nav_graph.xml` was created.
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  xmlns:tools="http://schemas.android.com/tools"
  android:id="@+id/nav_graph">

</navigation>
```

Next, the three fragmens `CompaniesFragment`, `RoutseFragment`, and `StopsFragment` were defined and `CompaniesFragment` was set as the `startDestination`.
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  xmlns:tools="http://schemas.android.com/tools"
  android:id="@+id/nav_graph"
  app:startDestination="@id/CompanyFragment">

  <fragment
    android:id="@+id/CompanyFragment"
    android:name="com.riis.jetpacketa.features.company.CompaniesFragment"
    android:label="@string/label_companies_fragment"
    tools:layout="@layout/fragment_company">
  </fragment>

  <fragment
    android:id="@+id/RoutesFragment"
    android:name="com.riis.jetpacketa.features.route.RoutesFragment"
    android:label="@string/label_routes_fragment"
    tools:layout="@layout/fragment_route">
  </fragment>

  <fragment
    android:id="@+id/StopsFragment"
    android:name="com.riis.jetpacketa.features.stop.StopsFragment"
    android:label="@string/label_stops_fragment"
    tools:layout="@layout/fragment_stops">
  </fragment>
</navigation>
```

Finally, actions were setup that allowed the `CompaniesFragment` to show the `RoutesFrgment` and the `RoutesFragment` to show the `StopsFragment`
```xml
  ...

  <fragment
    android:id="@+id/CompanyFragment"
    android:name="com.riis.jetpacketa.features.company.CompaniesFragment"
    android:label="@string/label_companies_fragment"
    tools:layout="@layout/fragment_company">

    <action
      android:id="@+id/action_CompaniesFragment_to_RoutesFragment"
      app:destination="@id/RoutesFragment" />
  </fragment>

  <fragment
    android:id="@+id/RoutesFragment"
    android:name="com.riis.jetpacketa.features.route.RoutesFragment"
    android:label="@string/label_routes_fragment"
    tools:layout="@layout/fragment_route">

    <action
      android:id="@+id/action_RoutesFragment_to_StopsFragment"
      app:destination="@id/StopsFragment" />
  </fragment>


  ...

```

## MainActivity
Now that the `NavGraph` has been created, `MainActivity` needs to use it. In the `activity_main.xml`, the `FrameLayout` was replaced with a `fragment` element that points to the `NavGraph`.
```xml
  ...

  <fragment
    android:id="@+id/nav_host_fragment_container"
    android:name="androidx.navigation.fragment.NavHostFragment"
    app:layout_constraintTop_toBottomOf="@id/appBar"
    app:layout_constraintBottom_toBottomOf="parent"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    app:navGraph="@navigation/nav_graph"
    app:defaultNavHost="true"/>

  ...
```

In `MainActivity`'s `onCreate` function, the navigation controller is setup
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  
  ...

  val navController = findNavController(R.id.nav_host_fragment_container)
  appBarConfiguration = AppBarConfiguration(navController.graph)
  setupActionBarWithNavController(navController, appBarConfiguration)
}
```

In addition, the `onSupportNavigate` was overridden to use the new navigation controller
```kotlin
override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_container)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
}
```

## Fragments
In the `CompaniesFragment` and the `RoutesFragment`, the `supportFragmentManager` transactions can be replaced. In the `CompaniesFragment`, the following code replaced the original `supportFragmentManager` transaction
```kotlin
findNavController()
  .navigate(
    R.id.action_CompaniesFragment_to_RoutesFragment,
    bundleOf("companyId" to it.id, "companyName" to it.name)
  )
```