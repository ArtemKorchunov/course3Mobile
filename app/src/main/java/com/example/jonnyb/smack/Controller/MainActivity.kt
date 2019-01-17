package com.example.jonnyb.smack.Controller

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.jonnyb.smack.Adapters.DeviceRecycleAdapter
import com.example.jonnyb.smack.Model.Device
import com.example.jonnyb.smack.R
import com.example.jonnyb.smack.Services.DeviceService
import com.example.jonnyb.smack.Services.SensorService
import com.example.jonnyb.smack.Services.UserDataService
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import io.socket.client.Manager
import java.net.URI
import java.net.URISyntaxException
import org.json.JSONObject
import android.support.v4.app.FragmentActivity
import android.util.Log
import io.socket.emitter.Emitter
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.example.jonnyb.smack.Utilities.NotificationUtils


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: DeviceRecycleAdapter
    private lateinit  var notifManager: NotificationManager

    private fun setupAdapters() {
        adapter = DeviceRecycleAdapter(this, DeviceService.devices) { device ->
            onDeviceListItemClicked(device)
        }
        deviceListView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        deviceListView.layoutManager = layoutManager
        deviceListView.setHasFixedSize(true)
    }

    private fun setSensorDropdown(dialogView: View) {
        val sensorDropdown = dialogView.findViewById<Spinner>(R.id.spinner)
        val sensorNames = ArrayList<String>()
        for (sensor in SensorService.sensors) {
            sensorNames.add(sensor.name)
        }

        val arrayAdapter =  ArrayAdapter(this, android.R.layout.simple_spinner_item, sensorNames)
        sensorDropdown.adapter = arrayAdapter
    }

    private fun socketConnect() {
        val managerOptions = Manager.Options()
        managerOptions.path = "/listen"
        val options = IO.Options()
        options.query = "token=${App.prefs.authToken}"
        try {
            // 192.168.0.108 192.168.43.228
            val manager = Manager(URI("http://192.168.43.228:4000"), managerOptions)
            val namespaceSocket = Socket(manager, "/account",  options)
            namespaceSocket.connect()
            namespaceSocket.on("payload", onNewMessage)

        } catch (err: URISyntaxException) {
            print(2)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        createChannel()
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (App.prefs.isLoggedIn) {
            socketConnect()
            loginBtnNavHeader.text = "Logout"
            mainChannelName.text = "You are logged in"
            DeviceService.get(0, 5) { response ->
                if (response) {
                    setupAdapters()
                } else {
                    errorToast()
                }

            }
            SensorService.get() {response ->
                if(response) {
                } else {
                    errorToast()
                }

            }

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

            // The id of the channel.
            val id = "my_channel_01";

            // The user-visible name of the channel.
            val name = "lalallala";

            // The user-visible description of the channel.
            val description = "fsdafsdfs"

            val importance = NotificationManager.IMPORTANCE_LOW;

            val mChannel = NotificationChannel(id, name, importance);

            // Configure the notification channel.
            mChannel.setDescription(description);

            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);

            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(longArrayOf( 100, 200, 300, 400, 500, 400, 300, 200, 400 ));

            notifManager.createNotificationChannel(mChannel);
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
            print(3)
        val obj = args[0] as JSONObject
        val heat = obj["heat"]
        val id = "my_channel_01";
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val mBuilder = Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setContentTitle("Your device is unstable")
                    .setContentText("Device temperature is now ${heat}! °C")
                    .setChannelId(id)
                    .build();
            notifManager.notify(1, mBuilder)

        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

        }
    }

    fun onDeviceListItemClicked(device: Device) {

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

    fun addDeviceClicked(view: View) {
        if (App.prefs.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)
            setSensorDropdown(dialogView)
            builder.setView(dialogView)
                    .setPositiveButton("Add") { _, _ ->
                        val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
                        val descTextField = dialogView.findViewById<EditText>(R.id.addChannelDescTxt)
                        val statusSwitcher = dialogView.findViewById<Switch>(R.id.addDeviceSwitcher)
                        val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
                        val spinnerValue = spinner.getSelectedItem().toString()
                        val currentSensor = SensorService.sensors.find { sensor -> sensor.name == spinnerValue }
                        val channelName = nameTextField.text.toString()
                        val channelDesc = descTextField.text.toString()
                        val statusChecked = statusSwitcher.isChecked()
                        val device = Device(
                                (1000..70000).random(),
                                channelName,
                                channelDesc,
                                statusChecked
                        )
                        adapter.add(device)
                        DeviceService.post(device, currentSensor!!.id) {complete ->
                            if (!complete) errorToast()
                        }
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
