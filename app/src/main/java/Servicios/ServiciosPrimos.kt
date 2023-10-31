package Servicios

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.practica1_pmdm.ANB_numPrimos
import com.example.practica1_pmdm.R
import kotlin.random.Random


class ServiciosPrimos : AppCompatActivity() {

    lateinit var boton1 : Button
    lateinit var boton2 : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios_primos)

        //Iniciar servicio

        val serviceIntent = Intent(this, PrimosService::class.java)
        serviceIntent.putExtra("inputExta", 20)
        ContextCompat.startForegroundService(this, serviceIntent)


        //Botones
        Integer.MAX_VALUE/40000
        val randomColor = Random
        val color = Color.argb(255, randomColor.nextInt(), randomColor.nextInt(256), randomColor.nextInt(256))

        boton1 = findViewById(R.id.button)
        boton2 = findViewById(R.id.button2)

        boton2.setBackgroundColor(color)

        boton1.setOnClickListener {

            val intent = Intent(this,ANB_numPrimos::class.java)
            intent.putExtra("numero", 20)
            startActivityForResult(intent, 1)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val resultado = data?.getStringExtra("Resultado")
            Log.i("Resultado", resultado!!)
        }
    }
}