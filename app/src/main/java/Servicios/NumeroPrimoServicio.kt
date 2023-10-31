package Servicios

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.practica1_pmdm.R
import java.util.concurrent.Executors

class NumeroPrimoServicio : Service() {
    private val CHANNEL_ID = "PrimoCanaldeServicio"
    private lateinit var manager: NotificationManager
    private val notificationId = 101
    private val executor = Executors.newSingleThreadExecutor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        executor.execute {
            val maxNumber = Integer.MAX_VALUE / 40000
            val primeNumbers = cal_primos(maxNumber)

            val resultadoPrimosParaImprimir = "Numero primos: $primeNumbers"
            Log.d("PrimeNumbers", resultadoPrimosParaImprimir)

            showResultNotification(resultadoPrimosParaImprimir)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        executor.shutdownNow()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Numeros Primos Canal de Servicio",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun showResultNotification(resultadoPrimosParaImprimir: String) {
        val notificationIntent = Intent(this, ServiciosPrimos::class.java)
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

class PrimoService {

}
