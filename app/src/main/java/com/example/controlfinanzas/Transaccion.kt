package com.example.controlfinanzas

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Transaccion : AppCompatActivity() {

    lateinit var etMonto: EditText
    lateinit var etDescripcion: EditText
    lateinit var spCategoria: Spinner
    lateinit var etFecha: EditText
    lateinit var btnGuardar: Button
    lateinit var btnVolver: Button
    lateinit var tvTituloTransaccion: TextView

    var tipoTransaccion: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaccion)

        etMonto = findViewById(R.id.etMonto)
        etDescripcion = findViewById(R.id.etDescripcion)
        spCategoria = findViewById(R.id.spCategoria)
        etFecha = findViewById(R.id.etFecha)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnVolver = findViewById(R.id.btnVolver)
        tvTituloTransaccion = findViewById(R.id.tvTituloTransaccion)

        tipoTransaccion = intent.getStringExtra("TIPO_TRANSACCION")

        configurarCategorias()

        tvTituloTransaccion.text = if (tipoTransaccion == "entrada") {
            "Ingresar Entrada"
        } else {
            "Ingresar Gasto"
        }

        etFecha.setOnClickListener {
            mostrarDatePickerDialog()
        }

        btnGuardar.setOnClickListener {
            guardarTransaccion()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun configurarCategorias() {
        val categoriasEntrada = arrayOf("Nomina", "Negocios", "Regalos", "Otros")
        val categoriasSalida = arrayOf("Casa", "Comida", "Transporte", "Ropa", "Otros")

        val categorias = if (tipoTransaccion == "entrada") {
            categoriasEntrada
        } else {
            categoriasSalida
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, categorias
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter
    }


    private fun mostrarDatePickerDialog() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val fechaSeleccionada = "$dayOfMonth-${monthOfYear + 1}-$year"
                etFecha.setText(fechaSeleccionada)
            },
            anio, mes, dia
        )
        datePickerDialog.show()
    }

    private fun guardarTransaccion() {
        val monto = etMonto.text.toString().toDoubleOrNull()
        val descripcion = etDescripcion.text.toString()
        val categoria = spCategoria.selectedItem.toString()
        val fecha = etFecha.text.toString()

        if (monto == null || monto <= 0 || descripcion.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val tipo = if (tipoTransaccion == "entrada") "entrada" else "gasto"
        val result = dbHelper.insertarTransaccion(tipo, monto, categoria, descripcion, fecha)

        if (result != -1L) {
            Toast.makeText(this, "Transacción guardada exitosamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al guardar la transacción", Toast.LENGTH_SHORT).show()
        }
    }


    private fun limpiarCampos() {
        etMonto.text.clear()
        etDescripcion.text.clear()
        etFecha.text.clear()
        spCategoria.setSelection(0)
    }
}
