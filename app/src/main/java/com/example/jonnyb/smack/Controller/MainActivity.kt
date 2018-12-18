package com.example.jonnyb.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.jonnyb.smack.Adapters.DeviceRecycleAdapter
import com.example.jonnyb.smack.Model.Device
import com.example.jonnyb.smack.R
import com.example.jonnyb.smack.Services.DeviceService
import com.example.jonnyb.smack.Services.UserDataService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: DeviceRecycleAdapter

    private fun setupAdapters() {
        adapter = DeviceRecycleAdapter(this, DeviceService.devices) { device ->
            println(device)
        }
        deviceListView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        deviceListView.layoutManager = layoutManager
        deviceListView.setHasFixedSize(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if (App.prefs.isLoggedIn) {
            loginBtnNavHeader.text = "Logout"
            mainChannelName.text = "You are logged in"
            DeviceService.get(0, 5) { response ->
                if (response) {
                    setupAdapters()
                } else {
                    errorToast()
                }

            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked(view: View) {

        if (App.prefs.isLoggedIn) {
            UserDataService.logout()
            adapter.notifyDataSetChanged()
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNavHeader.text = "Login"
            mainChannelName.text = "Please log in"
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    fun addChannelClicked(view: View) {
        if (App.prefs.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)
                    .setPositiveButton("Add") { _, _ ->
                        val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
                        val descTextField = dialogView.findViewById<EditText>(R.id.addChannelDescTxt)
                        val channelName = nameTextField.text.toString()
                        val channelDesc = descTextField.text.toString()
                        val device = Device(
                                (1000..70000).random(),
                                channelName,
                                channelDesc,
                                false
                        )
                        adapter.add(device)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        // Cancel and close the dialog
                    }
                    .show()
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again.",
                Toast.LENGTH_LONG).show()
    }
}
