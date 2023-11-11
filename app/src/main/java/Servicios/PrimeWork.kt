import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.Data
import com.example.practica1_pmdm.R
import kotlin.math.sqrt

class PrimeWork : Worker {

    constructor(
        context: Context,
        params: WorkerParameters
    ) : super(context, params)

    private val notificationId = 105
    private val CHANNEL_ID = "WorkerManagerClass"
    private val manager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    override fun doWork(): Result {
        try {

            val primeNumbers = realizarTrabajo()
            showResultNotification("Números primos calculados: $primeNumbers")
            return Result.success()

        } catch (e: Exception) {

            Log.e("PrimeWorkerManager", "Error al realizar el trabajo", e)
            return Result.failure()
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

    private fun realizarTrabajo(): ArrayList<Int> {
        val maxNumber = Integer.MAX_VALUE / 40000
        val primeNumbers = calPrimos(maxNumber)

        for (primo in primeNumbers) {
            Log.d("PrimeNumbers", "Número primo: $primo")
        }

        Log.d("PrimeWorkerManager", "Trabajo realizado con éxito")

        return primeNumbers
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
