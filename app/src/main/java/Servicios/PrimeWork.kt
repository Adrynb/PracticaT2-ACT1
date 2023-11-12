package Servicios

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.practica1_pmdm.R
import org.greenrobot.eventbus.EventBus

import kotlin.math.sqrt

class PrimeWork(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val notificationId = 105
    private val CHANNEL_ID = "WorkerManagerClass"
    private val manager: NotificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    override fun doWork(): Result {
        return try {
            showResultNotification("Primo realizado con éxito")
            Result.success(doPrimeNumbers())
        } catch (e: Exception) {
            Log.e("PrimeWorkerManager", "Error al realizar el trabajo", e)
            Result.failure()
        }
    }

    private fun showResultNotification(resultadosImprimir: String) {
        val notification: Notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("PrimeService (WorkerManager)")
            .setContentText(resultadosImprimir)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        manager.notify(notificationId, notification)
    }

    private fun doPrimeNumbers(): Data {
        val maxNumber = Integer.MAX_VALUE / 40000
        val primeNumbers = calPrimos(maxNumber)

        for (primo in primeNumbers) {
            Log.d("PrimeNumbers", "Número primo: $primo")
        }

        Log.d("PrimeWorkerManager", "Trabajo realizado con éxito")

        val outputData = Data.Builder()
            .putIntArray("primos", primeNumbers.toIntArray())
            .build()

        return outputData
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Numeros Primos (WorkManager)",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(serviceChannel)
    }

    private fun calPrimos(n: Int): ArrayList<Int> {
        val elementos = ArrayList<Int>()

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
        for (i in 2..sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) {
                return false
            }
        }
        return true
    }

}
