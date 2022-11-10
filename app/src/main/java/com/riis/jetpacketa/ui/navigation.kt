package com.riis.jetpacketa.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.riis.jetpacketa.ui.screens.CompaniesScreenComposable
import com.riis.jetpacketa.ui.screens.RoutesScreenComposable
import com.riis.jetpacketa.ui.screens.StopsScreenComposable

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