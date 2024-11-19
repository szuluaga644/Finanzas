package com.example.controlfinanzas

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(Runnable {
            val splash = Intent(
                this@Splash,
                MainActivity::class.java
            )
            startActivity(splash)
            finish()
        }, 3000)
    }
}