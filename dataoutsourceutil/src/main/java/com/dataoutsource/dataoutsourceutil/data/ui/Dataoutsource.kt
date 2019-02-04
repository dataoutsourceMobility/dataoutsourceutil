package com.dataoutsource.dataoutsourceutil.data.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

open class Dataoutsource : AppCompatActivity(){

    private var preferences = "dataoutsourceprefs"
    private var rtFa = "rtFa"
    private var fedAuth = "FedAuth"

    val ticketingUrl = "https://dataoutsource.sharepoint.com/sites/ticketing/"
    val traingUrl = "https://dataoutsource.sharepoint.com/sites/Training/"

    fun getPrefs() = getSharedPreferences(preferences, Context.MODE_PRIVATE)

    fun checkCookies() : Boolean{
        var isCookiesPresent = false

        if(getPrefs().getString(rtFa, "") != ""
            && getPrefs().getString(fedAuth, "") != ""){
            isCookiesPresent = true
        }

        return isCookiesPresent
    }

    fun saveCookies(cookies: String){
        val editor = getPrefs().edit()
        val cookieArray = cookies.split(';')

        for(cookies in cookieArray){
            if(cookies.contains("rtfa", true)){
                editor.putString(rtFa, cookies)
            }
            if(cookies.contains("FedAuth", true)){
                editor.putString(fedAuth, cookies)
            }
        }
        editor.apply()
    }

    fun getCookies(): Pair<String, String>{
        return Pair(getPrefs().getString(rtFa,""), getPrefs().getString(fedAuth,""))
    }

    fun createJsonBody(params: ArrayList<Pair<String,String>>, listItemEntityType: String = "") : String{

        val jsonObj = JSONObject()

        if(listItemEntityType.isNotEmpty()){
            var metadata = JSONObject()
            metadata.put("type", listItemEntityType)

            jsonObj.put("__metadata", metadata)
        }

        params.forEach {
            jsonObj.put(it.first, it.second)
        }

        return jsonObj.toString()
    }
}