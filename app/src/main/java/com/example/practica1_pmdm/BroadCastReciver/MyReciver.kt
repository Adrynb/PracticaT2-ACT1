package com.example.practica1_pmdm.BroadCastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.widget.Toast

class MyReceiver(private val textView: TextView) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action

        if (action == Intent.ACTION_BATTERY_LOW) {
            val mensaje = "Estado actual: Batería Baja"
            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
            textView.text = mensaje
        } else if (action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            val mensaje = if (isAirplaneModeOn) {
                "Estado actual: Modo Avión Activado"
            } else {
                "Estado actual: Modo Avión Desactivado"
            }
            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
            textView.text = mensaje
        }
    }
}
