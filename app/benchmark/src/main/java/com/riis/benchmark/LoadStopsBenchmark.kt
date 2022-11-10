package com.riis.benchmark

import android.os.Trace
import androidx.benchmark.macro.*
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMetricApi::class)
class LoadStopsBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

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

}