package com.riis.jetpacketa.ui

sealed class Screen(val route: String) {
    object CompaniesScreen : Screen("companies_screen")
    object RoutesScreen : Screen("routes_screen")
    object StopsScreen : Screen("stops_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}
