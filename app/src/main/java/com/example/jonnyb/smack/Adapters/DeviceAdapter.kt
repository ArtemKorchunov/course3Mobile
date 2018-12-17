package com.example.jonnyb.smack.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.jonnyb.smack.Model.Device
import com.example.jonnyb.smack.R

/**
 * Created by jonnyb on 8/21/17.
 */
class DeviceAdapter(val context: Context, val devices: List<Device>) : BaseAdapter(){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val deviceView: View
        val holder : ViewHolder

        if (convertView == null) {
            deviceView = LayoutInflater.from(context).inflate(R.layout.device_list_item, null)
            holder = ViewHolder()
            holder.deviceName = deviceView?.findViewById<TextView>(R.id.name)
            holder.deviceDescription = deviceView?.findViewById<TextView>(R.id.description)
            holder.deviceStatus = deviceView?.findViewById<TextView>(R.id.status)
            deviceView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            deviceView = convertView
        }

        val device = devices[position]
        holder.deviceName?.text = device.name
        holder.deviceDescription?.text = device.description
        holder.deviceStatus?.text = device.status.toString()
        return deviceView
    }

    override fun getItem(position: Int): Any {
        return devices[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return devices.count()
    }

    private class ViewHolder {
        var deviceName: TextView? = null
        var deviceDescription: TextView? = null
        var deviceStatus: TextView? = null
    }
}