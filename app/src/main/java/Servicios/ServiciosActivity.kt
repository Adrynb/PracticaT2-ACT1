
package Servicios
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.practica1_pmdm.ANB_numPrimos
import com.example.practica1_pmdm.R
import kotlin.random.Random


class ServiciosActivity : AppCompatActivity() {

    lateinit var boton1 : Button
    lateinit var boton2 : Button
    lateinit var botonServicio : Button
    private var enable: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios_primos)

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

        /*
        Segundo Plano
        val secondServiceIntent = Intent(this, PrimoSecondService::class.java)
        startService(secondServiceIntent)

*/
        //IntentService

        val intentService = Intent(this, MyIntentService::class.java)
        startService(intentService)

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
}