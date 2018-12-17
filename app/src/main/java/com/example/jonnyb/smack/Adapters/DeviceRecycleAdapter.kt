package com.example.jonnyb.smack.Adapters

import android.support.v7.widget.RecyclerView
import com.example.jonnyb.smack.Model.Device
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jonnyb.smack.R

class DeviceRecycleAdapter(val context: Context, val devices: List<Device>, val itemClick: (Device) -> Unit) : RecyclerView.Adapter<DeviceRecycleAdapter.Holder>(){
    override fun onBindViewHolder(holder: DeviceRecycleAdapter.Holder?, position: Int) {
        holder?.bindCategory(devices[position], context)
    }

    override fun getItemCount(): Int {
        return devices.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeviceRecycleAdapter.Holder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.device_list_item, parent, false)
        return Holder(view, itemClick)
    }

    inner class Holder(itemView: View?, val itemClick: (Device) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val deviceName = itemView?.findViewById<TextView>(R.id.name)
        val deviceDescription = itemView?.findViewById<TextView>(R.id.description)
        val deviceStatus = itemView?.findViewById<TextView>(R.id.status)

        fun bindCategory(device: Device, context: Context) {
            deviceName?.text = device.name
            deviceDescription?.text = device.description
            deviceStatus?.text = device.status.toString()
            itemView.setOnClickListener { itemClick(device) }
        }
    }
}