package com.example.jonnyb.smack.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.jonnyb.smack.Controller.App
import com.example.jonnyb.smack.Model.Device
import com.example.jonnyb.smack.Utilities.URL_GET_DEVICE
import com.example.jonnyb.smack.Utilities.URL_POST_DEVICE
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

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

    fun post(device: Device, sensorId: Int, complete: (Boolean) -> Unit) {
        val jsonArray = JSONArray()
        jsonArray.put(1)
        val jsonBody = JSONObject()
        jsonBody.put("id", device.id)
        jsonBody.put("name", device.name)
        jsonBody.put("sensor_id", sensorId)
        jsonBody.put("description", device.description)
        jsonBody.put("status", device.status)
        jsonBody.put("charts", jsonArray)

        val requestBody = jsonBody.toString()

        val createRequest = object: JsonObjectRequest(Method.POST, URL_POST_DEVICE, null, Response.Listener { response ->
            complete(true)
        }, Response.ErrorListener { error ->
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(createRequest)
    }

    fun delete(id: Int) {
        val deleteRequest = object: JsonObjectRequest(Method.DELETE, "$URL_POST_DEVICE/$id", null, Response.Listener { response ->
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user: $error")
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

        App.prefs.requestQueue.add(deleteRequest)
    }
}