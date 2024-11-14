package com.example.controlfinanzas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class Historial : AppCompatActivity() {

    private lateinit var tvTituloMes: TextView
    private lateinit var lvTransacciones: ListView
    private lateinit var btnMesAnterior: Button
    private lateinit var btnMesSiguiente: Button
    private lateinit var btnVolver: Button

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    // Lista de transacciones
    private val transacciones = mutableListOf<Transaccion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial)

        tvTituloMes = findViewById(R.id.tvTituloMes)
        lvTransacciones = findViewById(R.id.lvTransacciones)
        btnMesAnterior = findViewById(R.id.btnMesAnterior)
        btnMesSiguiente = findViewById(R.id.btnMesSiguiente)
        btnVolver = findViewById(R.id.btnVolver)

        // Llamamos a la función para agregar datos de ejemplo
        agregarTransaccionesDeEjemplo()

        // Configuramos el adaptador para el ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transacciones.map {
            "${it.tipoTransaccion}: ${it.etMonto} - ${it.spCategoria} - ${it.etFecha}"
        })
        lvTransacciones.adapter = adapter

        // Actualizamos el título del mes
        actualizarMes()

        // Manejadores de clic para los botones
        btnMesAnterior.setOnClickListener {
            cambiarMes(-1)
        }

        btnMesSiguiente.setOnClickListener {
            cambiarMes(1)
        }

        btnVolver.setOnClickListener {
            finish() // Vuelve a la actividad anterior
        }
    }

    // Función para actualizar el mes en el título
    private fun actualizarMes() {
        val mesActual = dateFormat.format(calendar.time)
        tvTituloMes.text = "Mes: $mesActual"
    }

    // Función para cambiar el mes (anterior o siguiente)
    private fun cambiarMes(incremento: Int) {
        calendar.add(Calendar.MONTH, incremento)
        actualizarMes()
    }

    // Función para agregar transacciones de ejemplo
    private fun agregarTransaccionesDeEjemplo() {
        transacciones.clear()
    }
}

