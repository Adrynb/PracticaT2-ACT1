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
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.sqrt

class PrimeWorkBus(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val notificationId = 107
    private val CHANNEL_ID = "WorkerManagerClass"
    private val manager: NotificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var numerosPrimos: ArrayList<Int> = ArrayList()

    init {
        createNotificationChannel()
        EventBus.getDefault().register(this)
    }

    override fun doWork(): Result {
        try {
            val outputData = doPrimeNumbers()

            val primeNumbers = outputData.getIntArray("primos")?.toList() ?: emptyList()

            showResultNotification(primeNumbers)

            val primeNumbersString = buildPrimeNumbersString(primeNumbers)

            EventBus.getDefault().post(primeNumbersString)

            return Result.success()
        } catch (e: Exception) {
            Log.e("PrimeWorkBus", "Error al realizar el trabajo", e)
            return Result.failure()
        }
    }

    private fun showResultNotification(primeNumbers: List<Int>) {

        val primeNumbersString = buildPrimeNumbersString(primeNumbers)

        val notification: Notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("PrimeService (WorkerManager)")
            .setContentText(primeNumbersString)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        manager.notify(notificationId, notification)
    }

    private fun buildPrimeNumbersString(primeNumbers: List<Int>): String {
        val stringBuilder = StringBuilder()
        for (prime in primeNumbers) {
            stringBuilder.append("$prime, ")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 2)
        return stringBuilder.toString()
    }

    private fun doPrimeNumbers(): Data {
        val maxNumber = 1000
        numerosPrimos = calPrimos(maxNumber)

        for (primo in numerosPrimos) {
            Log.d("PrimeNumbers", "Número primo: $primo")
        }

        Log.d("PrimeWorkBus", "Trabajo realizado con éxito")

        val outputData = Data.Builder()
            .putIntArray("primos", numerosPrimos.toIntArray())
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

    fun getNumerosPrimos(): ArrayList<Int> {
        return numerosPrimos
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(primeNumbers: List<Int>) {
        Log.d("Bus Evento", primeNumbers.joinToString())
    }

}

