package Servicios

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.practica1_pmdm.Actividades.ANB_numPrimos
import com.example.practica1_pmdm.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.random.Random

class ServiciosActivity : AppCompatActivity() {

    lateinit var boton1: Button
    lateinit var boton2: Button
    lateinit var botonServicio: Button
    lateinit var botonSecondService: Button
    lateinit var botonIntentService: Button
    lateinit var botonWorkerManager: Button
    lateinit var botonEventBus: Button
    private var enable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios_primos)

        boton1 = findViewById(R.id.button)
        boton2 = findViewById(R.id.button2)
        botonServicio = findViewById(R.id.primoService)
        botonSecondService = findViewById(R.id.secondService)
        botonIntentService = findViewById(R.id.intentService)
        botonWorkerManager = findViewById(R.id.workManager)
        botonEventBus = findViewById(R.id.eventBus)

        asignarColorAleatorio(boton1)
        asignarColorAleatorio(botonServicio)
        asignarColorAleatorio(botonSecondService)
        asignarColorAleatorio(botonIntentService)
        asignarColorAleatorio(botonWorkerManager)
        asignarColorAleatorio(botonEventBus)

        //Boton 1
        boton1.setOnClickListener {
            val intent = Intent(this, ANB_numPrimos::class.java)
            intent.putExtra("numero", 20)
            startActivityForResult(intent, 1)
        }

        //Primer Servicio

        botonServicio.setOnClickListener {
            val intentService = Intent(this, PrimoService::class.java)
            startService(intentService)
        }

        //Segundo Servicio

        botonSecondService.setOnClickListener {
            val secondServiceIntent = Intent(this, PrimoSecondService::class.java)
            startService(secondServiceIntent)
        }

        //Intent Service

        botonIntentService.setOnClickListener {
            val intentService = Intent(this, MyIntentService::class.java)
            startService(intentService)
        }

        //WorkerManager
        botonWorkerManager.setOnClickListener {
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



        }

        //Event Bus
        botonEventBus.setOnClickListener {
            val intent = Intent(this, PrimeWorkBus::class.java)
            val workManager: WorkManager = WorkManager.getInstance(this)
            val workRequest = OneTimeWorkRequest.Builder(PrimeWorkBus::class.java).build()
            workManager.enqueue(workRequest)


        }


    }
    private fun asignarColorAleatorio(button: Button) {
        val randomColor = Random
        val color = Color.argb(255, randomColor.nextInt(256), randomColor.nextInt(256), randomColor.nextInt(256))
        button.setBackgroundColor(color)
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

        // Resto de tu lógica relacionada con EventBus...
    }
}
