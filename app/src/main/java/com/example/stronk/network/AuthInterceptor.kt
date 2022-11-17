package com.example.stronk.network

import android.content.Context
import android.content.SharedPreferences
import com.example.stronk.R
import com.example.stronk.model.MyRoutinesViewModel
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val preferencesManager = PreferencesManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        preferencesManager.fetchAuthToken()?.let {
            println("Fetched token $it")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}

class PreferencesManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val MY_ROUTINES_VIEW_PREFERENCE= "my_routines_view_preference"
        const val EXPLORE_VIEW_PREFERENCE= "explore_view_preference"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
        println("Saved token $token")
    }

    fun removeAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
        println("Removed token")
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    enum class ViewPreference(val value:String) {
        LIST("List") , GRID("Grid")
    }

    fun saveViewPreferenceMyRoutines(viewPreference: ViewPreference) {
        val editor = prefs.edit()
        editor.putString(MY_ROUTINES_VIEW_PREFERENCE, viewPreference.value)
        editor.apply()
        println("Saved view preference $viewPreference")
    }

    fun fetchViewPreferenceMyRoutines(): ViewPreference {
        val viewPreference = prefs.getString(MY_ROUTINES_VIEW_PREFERENCE, ViewPreference.LIST.value)
        return ViewPreference.values().first { it.value == viewPreference }
    }

    fun saveViewPreferenceExplore( viewPreference: ViewPreference){
        val editor = prefs.edit()
        editor.putString(EXPLORE_VIEW_PREFERENCE, viewPreference.value)
        editor.apply()
        println("Saved view preference $viewPreference")
    }

    fun fetchViewPreferenceExplore(): ViewPreference {
        val viewPreference = prefs.getString(EXPLORE_VIEW_PREFERENCE, ViewPreference.GRID.value)
        return ViewPreference.values().first { it.value == viewPreference }
    }

}