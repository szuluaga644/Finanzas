package com.example.controlfinanzas

import android.content.Intent
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
    private val transacciones = mutableListOf<TransaccionModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial)

        tvTituloMes = findViewById(R.id.tvTituloMes)
        lvTransacciones = findViewById(R.id.lvTransacciones)
        btnMesAnterior = findViewById(R.id.btnMesAnterior)
        btnMesSiguiente = findViewById(R.id.btnMesSiguiente)
        btnVolver = findViewById(R.id.btnVolver)

        agregarTransacciones()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transacciones.map {
            "${it.tipoTransaccion}: ${it.etMonto} - ${it.spCategoria} - ${it.etFecha}"
        })
        lvTransacciones.adapter = adapter

        actualizarMes()

        btnMesAnterior.setOnClickListener {
            cambiarMes(-1)
        }

        btnMesSiguiente.setOnClickListener {
            cambiarMes(1)
        }

        btnVolver.setOnClickListener {
            finish()
        }

        lvTransacciones.setOnItemClickListener { _, _, position, _ ->
            val transaccion = transacciones[position]
            val intent = Intent(this, DetalleTransaccion::class.java)
            intent.putExtra("TIPO_TRANSACCION", transaccion.tipoTransaccion)
            intent.putExtra("MONTO", transaccion.etMonto)
            intent.putExtra("CATEGORIA", transaccion.spCategoria)
            intent.putExtra("DESCRIPCION", transaccion.etDescripcion)
            intent.putExtra("FECHA", transaccion.etFecha)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        agregarTransacciones()
    }

    private fun cambiarMes(incremento: Int) {
        calendar.add(Calendar.MONTH, incremento)
        actualizarMes()
        agregarTransacciones()
    }

    private fun actualizarMes() {
        val mesActual = dateFormat.format(calendar.time)
        tvTituloMes.text = "Mes: $mesActual"
    }

    private fun agregarTransacciones() {
        val dbHelper = DatabaseHelper(this)
        val cursor = dbHelper.obtenerTransacciones()

        transacciones.clear()

        val mesActual = calendar.get(Calendar.MONTH)
        val anioActual = calendar.get(Calendar.YEAR)

        while (cursor.moveToNext()) {
            val tipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIPO))
            val monto = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MONTO))
            val categoria = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORIA))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPCION))
            val fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FECHA))

            val formatoFecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val fechaTransaccion = formatoFecha.parse(fecha)
            val calendarTransaccion = Calendar.getInstance()
            calendarTransaccion.time = fechaTransaccion

            val mesTransaccion = calendarTransaccion.get(Calendar.MONTH)
            val anioTransaccion = calendarTransaccion.get(Calendar.YEAR)

            if (mesTransaccion == mesActual && anioTransaccion == anioActual) {
                transacciones.add(TransaccionModel(tipo, monto, categoria, descripcion, fecha))
            }
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transacciones.map {
            "${it.tipoTransaccion}: ${it.etMonto} - ${it.spCategoria} - ${it.etFecha}"
        })
        lvTransacciones.adapter = adapter
    }
}

