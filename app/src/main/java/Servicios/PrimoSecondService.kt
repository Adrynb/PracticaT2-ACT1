package Servicios

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class PrimoSecondService : Service() {
    private var enable : Boolean = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        this.run()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun run() {
        enable = true

        Thread{
            var contador = 0;
            while(enable){
                Log.e("Servicio" ,"Servicio en segundo plano esta en funcionamiento" +contador)
            }
            try {
                Thread.sleep(2000)
                contador++
                if (contador > 10){
                    stopSelf()
                }
            } catch (e: InterruptedException){
                e.printStackTrace()
            }
        }.start()

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}