# Benchmark
## Setup
To setup the `Benchmark` jetpack, this [guide](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview) from the Android developer documentation was followed. This will create a new module in the project to contain `macro` and/or `micro` benchmark tests.

In addition, the following line was added to the `project` level `build.gradle`
```gradle
plugins {

  ...

  id 'com.android.test' version '7.3.1' apply false
}
```

## Macro Benchmark
In this project, a `macro` benchmark was created to time how long it takes to populate the `companies`, `routes`, and `stops` recycler views with data from the database. In the new `benchmark` module, a new file called `LoadStopsBenchmark.kt` was created and setup for a `macro` benchmark
```kotlin
@LargeTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMetricApi::class)
class LoadStopsBenchmark {
  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

}
```

Next, a simple test to load the app once was created
```kotlin
...

@Test
fun startup() = benchmarkRule.measureRepeated(
    packageName = "com.riis.jetpacketa",
    metrics = listOf(StartupTimingMetric()),
    iterations = 1,
    setupBlock = {
        // Press home button before each run to ensure the starting activity isn't visible.
        pressHome()
    }
) {
    // starts default launch activity
    startActivityAndWait()

}
```
> Note: To run the test, the `Build Variant` of the application must be switched from `debug` to `benchmark`

Next, to benchmark the load times, the test `loadRoutes` was created. It will run 4 iterations. In each iteration, a different route will be loaded. In addition, after each iteration, the `back` button will be pressed twice to return the phone back to the `companies` fragment. This prepares the device for the next iteration.
```kotlin
...

@Test
fun loadRoutes() {
    benchmarkRule.measureRepeated(
        packageName = "com.riis.jetpacketa",
        metrics = listOf(TraceSectionMetric("RV Load Routes")),
        iterations = 4,
        setupBlock = {
            device.pressBack()
            device.pressBack()
            pressHome()
            startActivityAndWait()
        }
    ) {
        Trace.beginSection("RV Load Routes")
        val companyRecyclerView = device.findObject(By.res("com.riis.jetpacketa", "companyRecyclerView"))
        companyRecyclerView.children[0].click()

        val routesRecyclerView = device.findObject(By.res("com.riis.jetpacketa", "routeRecyclerView"))
        routesRecyclerView.children[iteration ?: 0].click()

        check(device.wait(Until.hasObject(By.res("com.riis.jetpacketa", "stopsRecyclerView")), 2000)) {
            "RecyclerView not found after waiting 2000 ms."
        }
        val stopsRecyclerView = device.findObject(By.res("com.riis.jetpacketa", "stopsRecyclerView"))
        check(stopsRecyclerView.children.size > 0) {
            "RecyclerView not populated"
        }
        Trace.endSection()
    }
}
```