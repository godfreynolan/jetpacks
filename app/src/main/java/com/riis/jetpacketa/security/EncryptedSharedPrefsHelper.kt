package com.riis.jetpacketa.security

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import java.lang.reflect.Type


class EncryptedSharedPrefsHelper(private val context: Context) {

    companion object {
        private const val TAG = "EncryptedSharedPrefs"
        private const val FILE_NAME = "jetpack_eta_encrypted_shared_prefs"
    }

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPrefs = EncryptedSharedPreferences.create(
        FILE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Saves an Object to Encrypted Shared Preferences
     */
    fun <T> savePreference(key: String, value: T) {
        val gson = Gson()
        val save = gson.toJson(value)
        savePreference(key, save)
    }

    /**
     * Saves a string to Encrypted Shared Preferences
     */
    private fun savePreference(key: String, value: String): Boolean {
        return sharedPrefs
            .edit()
            .putString(key, value)
            .commit()
    }

    fun <T> getPreference(key: String, ofClass: Type): T? {
        val retrieved = sharedPrefs.getString(key, "") ?: return null
        return Gson().fromJson(retrieved, ofClass)
    }
}