package com.example.jonnyb.smack.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.jonnyb.smack.Controller.App
import com.example.jonnyb.smack.Model.Device
import com.example.jonnyb.smack.Utilities.URL_GET_DEVICE
import org.json.JSONException

object DeviceService {
    val devices= ArrayList<Device>()

    fun get(page: Int, count: Int, complete: (Boolean) -> Unit) {
        val registerRequest = object : JsonObjectRequest(Method.GET, "$URL_GET_DEVICE?page=${page.toString()}&count=${count.toString()}", null, Response.Listener { response ->
            try {
                val data = response.getJSONArray("data")
                for (i in 0..(data.length() - 1)) {
                    val item = data.getJSONObject(i)
                    val device = Device(
                        item.getInt("id"),
                        item.getString("name"),
                        item.getString("description"),
                        item.getBoolean("status")
                    )
                    devices.add(device)
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