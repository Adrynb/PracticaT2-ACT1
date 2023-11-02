package Servicios

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.practica1_pmdm.R
import java.util.concurrent.Executors

class PrimoService : Service() {
    private val CHANNEL_ID = "PrimoCanaldeServicio"
    private lateinit var manager: NotificationManager
    private val notificationId = 101
    private val executor = Executors.newSingleThreadExecutor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notification = createForegroundNotification()
        startForeground(notificationId, notification)

        executor.execute {
            val maxNumber = Integer.MAX_VALUE / 40000
            val primeNumbers = cal_primos(maxNumber)

            val resultadoPrimosParaImprimir = "Número primos: $primeNumbers"
            showResultNotification(resultadoPrimosParaImprimir)

            stopForeground(true)
            stopSelf()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Numeros Primos Canal de Servicio",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createForegroundNotification(): Notification {
        val notificationIntent = Intent(this, ServiciosActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Servicio en Primer Plano")
            .setContentText("Calculando números primos...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun showResultNotification(resultadoPrimosParaImprimir: String) {
        val notificationIntent = Intent(this, ServiciosActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Resultado Números Primos")
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
