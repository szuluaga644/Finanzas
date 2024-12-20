package com.example.controlfinanzas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var tvTotalIngresos: TextView
    private lateinit var tvTotalGastos: TextView
    private lateinit var tvBalanceActual: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvTotalIngresos = findViewById(R.id.tvTotalIngresos)
        tvTotalGastos = findViewById(R.id.tvTotalGastos)
        tvBalanceActual = findViewById(R.id.tvBalanceActual)

        val btnIngresarEntrada: Button = findViewById(R.id.btnIngresarEntrada)
        val btnIngresarGasto: Button = findViewById(R.id.btnIngresarGasto)
        val btnVerHistorial: Button = findViewById(R.id.btnVerHistorial)
        val btnVerGraficos: Button = findViewById(R.id.btnVerGraficos)
        val btnSalir: Button = findViewById(R.id.btnSalir)

        actualizarValores()

        btnIngresarEntrada.setOnClickListener {
            val intent = Intent(this, Transaccion::class.java)
            intent.putExtra("TIPO_TRANSACCION", "entrada")
            startActivity(intent)
        }

        btnIngresarGasto.setOnClickListener {
            val intent = Intent(this, Transaccion::class.java)
            intent.putExtra("TIPO_TRANSACCION", "gasto")
            startActivity(intent)
        }

        btnVerHistorial.setOnClickListener {
            val intent = Intent(this, Historial::class.java)
            startActivity(intent)
        }

        //btnVerGraficos.setOnClickListener {
        //    val intent = Intent(this, Graficos::class.java)
        //    startActivity(intent)
        //}


        btnSalir.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarValores()
    }

    private fun actualizarValores() {
        val dbHelper = DatabaseHelper(this)
        val cursor = dbHelper.obtenerTransacciones()

        var totalIngresos = 0.0
        var totalGastos = 0.0

        while (cursor.moveToNext()) {
            val tipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIPO))
            val monto = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MONTO))
            if (tipo == "entrada") {
                totalIngresos += monto
            } else {
                totalGastos += monto
            }
        }
        cursor.close()

        val balanceActual = totalIngresos - totalGastos
        tvTotalIngresos.text = "Total Ingresos: $${totalIngresos}"
        tvTotalGastos.text = "Total Gastos: $${totalGastos}"
        tvBalanceActual.text = "Balance Actual: $${balanceActual}"
    }
}
