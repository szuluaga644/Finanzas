package com.example.controlfinanzas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class DetalleTransaccion : AppCompatActivity() {

    private lateinit var tvTipoTransaccion: TextView
    private lateinit var tvMonto: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvFecha: TextView
    private lateinit var btnEliminar: Button
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle)

        tvTipoTransaccion = findViewById(R.id.tvTipoTransaccion)
        tvMonto = findViewById(R.id.tvMonto)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvFecha = findViewById(R.id.tvFecha)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnVolver = findViewById(R.id.btnVolver)


        val tipoTransaccion = intent.getStringExtra("TIPO_TRANSACCION")
        val monto = intent.getDoubleExtra("MONTO", 0.0)
        val categoria = intent.getStringExtra("CATEGORIA")
        val descripcion = intent.getStringExtra("DESCRIPCION")
        val fecha = intent.getStringExtra("FECHA")

        tvTipoTransaccion.text = "Tipo: $tipoTransaccion"
        tvMonto.text = "Monto: $$monto"
        tvCategoria.text = "Categoría: $categoria"
        tvDescripcion.text = "Descripción: $descripcion"
        tvFecha.text = "Fecha: $fecha"

        btnEliminar.setOnClickListener {
            eliminarTransaccion()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun eliminarTransaccion() {
        val dbHelper = DatabaseHelper(this)
        val tipoTransaccion = intent.getStringExtra("TIPO_TRANSACCION")
        val monto = intent.getDoubleExtra("MONTO", 0.0)
        val categoria = intent.getStringExtra("CATEGORIA")
        val descripcion = intent.getStringExtra("DESCRIPCION")
        val fecha = intent.getStringExtra("FECHA")
        val result = dbHelper.eliminarTransaccion(tipoTransaccion, monto, categoria, descripcion, fecha)
        if (result > 0) {
            Toast.makeText(this, "Transacción eliminada", Toast.LENGTH_SHORT).show()
            finish()  // Volver a la pantalla anterior
        } else {
            Toast.makeText(this, "Error al eliminar la transacción", Toast.LENGTH_SHORT).show()
        }
    }
}
