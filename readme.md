# Compose
## Setup
To add `compose` to the project, the following dependencies were added to the `app` level `build.gradle`
```gradle
// Compose
def compose_version = "1.3.1"
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.runtime:runtime-livedata:$compose_version"
implementation "androidx.activity:activity-compose:$compose_version"
implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
implementation "androidx.compose.material:material:$compose_version"
androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
debugImplementation "androidx.compose.ui:ui-tooling:$compose_version"
debugImplementation "androidx.compose.ui:ui-test-manifest:$compose_version"
implementation "androidx.navigation:navigation-compose:2.6.0-alpha03"
```

## Project Structure
A package called `ui` was created in the app directory. Inside the `ui` package, three more packages were created: `screens`, `theme`, and `shared`.

## Theme
The `theme` folder will contain theming objects that can be provided to the composables created. In the `theme` folder, four files were created: `color.kt`, `shape.kt`, `theme.kt`, and `Type.kt`
<br>

`Color.kt`
```kotlin
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
```

`Shape.kt`
```kotlin
val Shapes = Shapes(
  small = RoundedCornerShape(8.dp),
  medium = RoundedCornerShape(5.dp),
  large = RoundedCornerShape(0.dp)
)
```

`Theme.kt`
```kotlin
private val DarkColorPalette = darkColors(
  primary = Purple200,
  primaryVariant = Purple700,
  secondary = Teal200
)

private val LightColorPalette = lightColors(
  primary = Purple500,
  primaryVariant = Purple700,
  secondary = Teal200
)

@Composable
fun SampleComposeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  MaterialTheme(
    colors = colors,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}
```

`Type.kt`
```kotlin
val Typography = Typography(
  body1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp
  )
)
```


## List View Items
### ListViewItem
The `recycler_view_item.xml` is shared across most of the recycler adapters. Therefore, a composable called `ListViewItem` was created inside the `shared` folder. 
<br>

The `preview` composable allows for quick rendering while writing the code. In the `ListViewItem` function, a `Row` element was created. Then, a `Card` composable was placed inside of it with the same styling attributes as `recycler_view_item.xml`. Finally, a `Text` composable was placed in the card to display the information.
```kotlin
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultListViewPreview() {
    ListViewItem(displayText = "SMART")
}

@Composable
fun ListViewItem(displayText: String, onClick: (() -> Unit)? = null) {
  Row {
    Card(
      elevation = 5.dp,
      shape = Shapes.medium,
      modifier = Modifier
        .padding(
            start = 10.dp,
            end = 10.dp,
            top = 5.dp,
            bottom = 5.dp
        )
        .fillMaxWidth()
        .clickable { onClick?.invoke() }
    ) {
      Text (
        text = displayText,
        style = Typography.body1,
        modifier = Modifier
          .widthIn(18.dp)
          .padding (
            start = 10.dp,
            end = 10.dp,
            top = 5.dp,
            bottom = 5.dp
        )
      )
    }
  }
}
```
### FavoriteListViewItem
For the `stops` fragment, there was a `favorite` button on the item. Therefore, a new file called `FavoriteListViewItem` was created inside the `shared` folder. It is very similar to `ListViewItem` but contains an `IconButton` with an `onClick` callback for the icon instead of the item itself.
```kotlin
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultFavoriteListViewPreview() {
    FavoriteStopListViewItem(stop = StopUi(32, "Test & Sample", false))
}

@Composable
fun FavoriteStopListViewItem(stop: StopUi, onClick: (() -> Unit)? = null) {
  Row {
    Card(
      elevation = 5.dp,
      shape = Shapes.medium,
      modifier = Modifier
        .padding(
            start = 10.dp,
            end = 10.dp,
            top = 5.dp,
            bottom = 5.dp
        )
        .fillMaxWidth()
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
      ) {
        Text (
          text = stop.stopName,
          style = Typography.body1,
          modifier = Modifier
            .padding(
              start = 10.dp,
              end = 10.dp,
              top = 5.dp,
              bottom = 5.dp
            )
        )
        IconButton(
            onClick = { onClick?.invoke() }
        ) {
          Icon(
            if(stop.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            "Favorite this item.",
            tint = MaterialTheme.colors.primary,
          )
        }
      }
    }
  }
}
```

## Screens
A `screens` folder was created inside of the `ui` folder to store all of the screens used by the application. Inside this folder, three files were created: `companies.kt`, `routes,kt`, and `stops,kt`.

### Company
In this file, three composables were made. The first composable displayed the information to be displayed on the screen. The second composable was a preview that supplied mock data to the first composable. Finally, the third composable was a list view containing individual `ListViewItem`s

The `DefaultCompaniesPreview` composable is responsible for rendering a preview of the screen with mock data.
```kotlin
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultCompaniesPreview() {
  val mockCompanyItems = mutableListOf<Company>(
    Company(1,"SMART","example.com","America/Detroit","en",null,null,null),
    Company(2,"DDOT","example.com","America/Detroit","en",null,null,null)
  )
  Surface {
    CompaniesListView(companies = mockCompanyItems) {}
  }
}
```

The `CompaniesScreenComposable` is responsible for displaying the entire screen. A `scaffold` was used to include a `TopAppBar` at the top of the screen and then the `CompaniesListView` below it. In addition, the `LiveData` in the `CompaniesViewModel` was casted `asFlow` and then collected. When the `LiveData` updates, only this composable will be re-composed.
```kotlin
@Composable
fun CompaniesScreenComposable(navController: NavController) {
    val viewModel = hiltViewModel<CompaniesViewModel>()
    viewModel.getCompanies()

    val companies: List<Company> by viewModel.companies.asFlow().collectAsState(initial = emptyList())

    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(text = "Companies") },
        )
      }, content = {
        Column(
            modifier = Modifier
              .padding(it)
              .fillMaxSize(),
        ) {
          CompaniesListView(companies = companies) { company ->
              navController.navigate(Screen.RoutesScreen.withArgs(company.id.toString(), company.name))
          }
        }
      }
    )
}
```
This `CompaniesListView` composable is responsible for creating the list of companies and propagating the `onClick` event to the main screen.
```kotlin
@Composable
fun CompaniesListView(companies: List<Company>, clicked: ((Company) -> (Unit))) {
  LazyColumn {
    items(companies) {
      ListViewItem(displayText = it.name) { clicked.invoke(it) }
    }
  }
}
```

### Route
The `route` screen behaves very similar to that of the `companies` screen.

#### Stop
The `stop` screen behaves very similar to that of the `companies` screen.

## Navigation
`Compose` uses `routes` to navigate between screens. Each `route` is defined by a string. In addition, bundle arguments are passed along on this `route` string. Therefore, to keep organization clean. A `Screen` sealed class was created to contain these routes and apply helper functions for initializing required bundle arguments. In the `ui` package, the `Screen` class was created.

```kotlin
sealed class Screen(val route: String) {
  object CompaniesScreen : Screen("companies_screen")
  object RoutesScreen : Screen("routes_screen")
  object StopsScreen : Screen("stops_screen")
}
```

Next requrired argumentes can be defined by appending `/<arguemnt_name>` to the end of the route. Therefore, a helper function called `withArgs` was created to easily append multiple required arguments.
```kotlin
sealed class Screen(val route: String) {
  ...

  /**
    * Appends required arguments to navigation routes
    * Idea from Philipp Lackner
    */
  fun withArgs(vararg args: String): String {
    return buildString {
      append(route)
      args.forEach { append("/$it") }
    }
  }
}
```
Now that the routes have been defined, a `Navigation` composable will handle showing the starting screen and providing the `navController` instance to the other screens. In the `ui` package, a `navigation.kt` file was created. This file describes three composables (one for each screen). Inside each `composable`, the routes and required arguments were defined, the types of the arguments were defined, and the `composable` to be presentented was selected.
```kotlin
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.CompaniesScreen.route) {
        composable(route = Screen.CompaniesScreen.route) {
            CompaniesScreenComposable(navController = navController)
        }
        composable(
            route = Screen.RoutesScreen.route + "/{companyId}/{companyName}",
            arguments = listOf(
                navArgument("companyId") {
                    type = NavType.IntType
                    nullable = false
                },
                navArgument("companyName") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            RoutesScreenComposable(
                navController = navController,
                companyId = entry.arguments?.getInt("companyId") ?: -1,
                companyName = entry.arguments?.getString("companyName") ?: ""
            )
        }
        composable(
            route = Screen.StopsScreen.route + "/{companyId}/{companyName}/{routeId}/{routeName}",
            arguments = listOf(
                navArgument("companyId") {
                    type = NavType.IntType
                    nullable = false
                },
                navArgument("companyName") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("routeId") {
                    type = NavType.IntType
                    nullable = false
                },
                navArgument("routeName") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            StopsScreenComposable(
                navController = navController,
                companyId = entry.arguments?.getInt("companyId") ?: -1,
                companyName = entry.arguments?.getString("companyName") ?: "",
                routeId = entry.arguments?.getInt("routeId") ?: -1,
                routeName = entry.arguments?.getString("routeName") ?: ""
            )
        }
    }
}
```

## MainActivity
Now, all that is left is to adjust the `MainActivity` to use `compose`. The `onCreate` function was replaced with
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent {
      SampleComposeAppTheme {
          Navigation()
      }
  }
}
```