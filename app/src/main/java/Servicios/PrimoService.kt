package Servicios

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Binder
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.practica1_pmdm.R

class PrimosService : Service() {

    private val binder = LocalBinder()
    private val CHANNEL_ID = "ForegroundServiceChannel"
    private lateinit var manager: NotificationManager
    inner class LocalBinder : Binder() {
        fun getService(): PrimosService {
            return this@PrimosService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val numero = intent?.getIntExtra("numero", 0)
        if (numero != null) {

            val resultado = cal_primos(numero)


            Log.i("Resultado", resultado.toString())
        }

        val notificationIntent = Intent(this, ServiciosPrimos::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notificacion")
            .setContentText("Iniciando servicio de primos")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .build()

         startForeground(3, notification)


        stopSelf()
        return START_NOT_STICKY
    }


    fun cal_primos(n: Int): ArrayList<Int> {
        var elementos = ArrayList<Int>()

        for (i in 1..n) {
            if (esPrimo(i, i - 1)) {
                elementos.add(i)
            }
        }

        return elementos
    }

    private fun esPrimo(n: Int, divisor: Int): Boolean {
        if (divisor <= 1) {
            return true
        } else if (n % divisor == 0) {
            return false
        } else {
            return esPrimo(n, divisor - 1)
        }
    }
}