package Servicios

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.practica1_pmdm.R

private const val ACTION_FOO = "Servicios.action.FOO"
private const val ACTION_BAZ = "Servicios.action.BAZ"
private const val EXTRA_PARAM1 = "Servicios.extra.PARAM1"
private const val EXTRA_PARAM2 = "Servicios.extra.PARAM2"

 class MyIntentService : IntentService("MyIntentService") {
    private val CHANNEL_ID = "MyIntentServiceChannel"
    private val NOTIFICATION_ID = 1
    private lateinit var manager: NotificationManager
    private val notificationId = 102

     override fun onHandleIntent(p0: Intent?) {
         val maxNumber = Integer.MAX_VALUE / 40000
         val primeNumbers = cal_primos(maxNumber)

         val resultadoPrimosParaImprimir = "Numero primos: $primeNumbers"
         Log.d("PrimeNumbers", resultadoPrimosParaImprimir)

         showResultNotification(resultadoPrimosParaImprimir)
         stopSelf()
     }
    private fun showResultNotification(resultadoPrimosParaImprimir: String) {
        val notificationIntent = Intent(this, ServiciosActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Resultado Numeros Primos")
            .setContentText(resultadoPrimosParaImprimir)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(notificationId, notification)
    }

    private fun hideNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    fun cal_primos(n: Int): ArrayList<Int> {
        var elementos = ArrayList<Int>()

        for (i in 2..n) {
            if (esPrimo(i)) {
                elementos.add(i)
            }
        }

        return elementos
    }

    private fun esPrimo(n: Int): Boolean {
        if (n <= 1) {
            return false
        }
        for (i in 2 until n) {
            if (n % i == 0) {
                return false
            }
        }
        return true
    }


 }



