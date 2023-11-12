
package Servicios

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.practica1_pmdm.ANB_numPrimos
import com.example.practica1_pmdm.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.random.Random


class ServiciosActivity : AppCompatActivity() {

    lateinit var boton1 : Button
    lateinit var boton2 : Button
    lateinit var botonServicio : Button
    private var enable: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios_primos)

        //Primer Plano

        /*
        if(!enable){
            val serviceIntent = Intent(this, PrimoService::class.java)
            startService(serviceIntent)
            ContextCompat.startForegroundService(this, serviceIntent)
            enable = true
        }
        else{
            enable = false
            val serviceIntent = Intent(this, PrimoService::class.java)
            stopService(serviceIntent)
        }
        */

       //Segundo plano

        /*
        val secondServiceIntent = Intent(this, PrimoSecondService::class.java)
        startService(secondServiceIntent)
         */



        //IntentService

        /*
        val intentService = Intent(this, MyIntentService::class.java)
        startService(intentService)
        */


        //WorkerManager

        /*
              val workerManager : WorkManager = WorkManager.getInstance(this)
        val workRequest = OneTimeWorkRequest.Builder(PrimeWork::class.java).build()
        workerManager.enqueue(workRequest)

        workerManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    val primosArray = workInfo.outputData.getIntArray("primos")
                    if (primosArray != null) {
                        for (primo in primosArray) {
                            Log.d("PrimeNumbers", "Número primo: $primo")
                        }
                    }
                }
            })
         */

        //EventBus
        val intent = Intent(this, PrimeWorkBus::class.java)
        val workManager: WorkManager = WorkManager.getInstance(this)
        val workRequest = OneTimeWorkRequest.Builder(PrimeWorkBus::class.java).build()
        workManager.enqueue(workRequest)



        val randomColor = Random
        val color = Color.argb(255, randomColor.nextInt(), randomColor.nextInt(256), randomColor.nextInt(256))

        boton1 = findViewById(R.id.button)
        boton2 = findViewById(R.id.button2)
        botonServicio = findViewById(R.id.service)

        botonServicio.setOnClickListener {
            val intentService = Intent(this, PrimoService::class.java)
            startService(intentService)
        }

        boton2.setBackgroundColor(color)

        boton1.setOnClickListener {

            val intent = Intent(this, ANB_numPrimos::class.java)
            intent.putExtra("numero", 20)
            startActivityForResult(intent, 1)
        }

    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PrimeWorkBus) {
        val numerosPrimos = event.getNumerosPrimos()

    }
}
