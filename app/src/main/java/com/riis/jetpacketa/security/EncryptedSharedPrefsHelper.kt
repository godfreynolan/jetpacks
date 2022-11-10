package com.riis.jetpacketa.security

import java.lang.reflect.Type

interface EncryptedSharedPrefsHelper {
    fun <T> savePreference(key: String, value: T)
    fun <T> getPreference(key: String, ofClass: Type): T?
}