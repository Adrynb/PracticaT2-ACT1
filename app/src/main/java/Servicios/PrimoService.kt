package Servicios

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Binder
import android.util.Log
import androidx.core.app.NotificationCompat

class PrimosService : Service() {

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): PrimosService {
            return this@PrimosService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Foreground Service Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val serviceChannel = NotificationChannel(CHANNEL_ID, channelName, importance)

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val numero = intent?.getIntExtra("numero", 0)
        if (numero != null) {

            val resultado = cal_primos(numero)


            Log.i("Resultado", resultado.toString())
        }

        val notificationIntent = Intent(this, ServiciosPrimos::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0,
            notificationIntent,  PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val notification : Notification = NotificationCompat.Builder(this, CHANNEL_ID)


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