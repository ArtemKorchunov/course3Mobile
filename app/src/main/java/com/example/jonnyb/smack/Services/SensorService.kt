package com.example.jonnyb.smack.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.jonnyb.smack.Controller.App
import com.example.jonnyb.smack.Model.Sensor
import com.example.jonnyb.smack.Utilities.URL_GET_SENSOR
import org.json.JSONException

object SensorService {
    val sensors = ArrayList<Sensor>()

    fun get(complete: (Boolean) -> Unit) {
        val registerRequest = object : JsonObjectRequest(Method.GET, URL_GET_SENSOR, null, Response.Listener { response ->
            try {
                val data = response.getJSONArray("data")
                for (i in 0..(data.length() - 1)) {
                    val item = data.getJSONObject(i)
                    val sensor = Sensor(
                            item.getInt("id"),
                            item.getString("name")
                    )
                    sensors.add(sensor)
                    // Your code here
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }

        }

        App.prefs.requestQueue.add(registerRequest)
    }
}