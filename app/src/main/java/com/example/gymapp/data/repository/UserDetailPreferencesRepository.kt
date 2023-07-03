package com.example.gymapp.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gymapp.ui.screen.viewmodel.enum.UserRegistrationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val TAG = "User Detail Preference Repository"
class UserDetailPreferencesRepository(
    private val dataStore: DataStore<Preferences>
): UserDetailRepository {

    private companion object {
        val USER_MOBILE_NUMBER_KEY = stringPreferencesKey("user_mobile_number_key")
        val USER_REGISTRATION_STATUS = stringPreferencesKey("user_registration_status_key")
    }

    override val userMobileNumber: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_MOBILE_NUMBER_KEY] ?: ""
        }

    override val userRegistrationStatus: Flow<UserRegistrationState> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {preferences ->
            enumValueOf<UserRegistrationState>(
                preferences[USER_REGISTRATION_STATUS] ?: UserRegistrationState.UNREGISTERED.name
            )
        }

    override suspend fun saveUserMobileNumber(mobileNumber: String) {
        Log.d(TAG, "entered saveUserMobileNumber ${mobileNumber} input")
        dataStore.edit {preferences ->
            preferences[USER_MOBILE_NUMBER_KEY] = mobileNumber
        }
        var output = userMobileNumber.first()
        Log.d(TAG, "Exiting saveUserMobileNumber flow userMobileNumber ${output}")
    }

    override suspend fun saveUserRegistrationState(userRegistrationState: UserRegistrationState) {
        dataStore.edit {preferences ->
            preferences[USER_REGISTRATION_STATUS] = userRegistrationState.name
        }
    }
}