package com.example.practica1_pmdm.Actividades

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practica1_pmdm.R

class ANB_numPrimos : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anb_numprimos)
        var numero = intent.getIntExtra("numero", -1)
        var returnIntent = Intent()
        var resultadoPrimo = cal_primos(numero)
        returnIntent.putExtra("Resultado", resultadoPrimo.toString())
        setResult(RESULT_OK, returnIntent)
        finish()

    }

    fun cal_primos(n: Int): ArrayList<Int> {
        var elementos = ArrayList<Int>()

        for (i in 1..n) {
            if (esPrimo(i, i - 1)) {
                elementos.add(i)
            }
        }

        return elementos
    }

    private fun esPrimo(n: Int, divisor: Int): Boolean {
        if (divisor <= 1) {
            return true
        } else if (n % divisor == 0) {
            return false
        } else {
            return esPrimo(n, divisor - 1)
        }
    }
}