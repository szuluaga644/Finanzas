package com.example.controlfinanzas

data class TransaccionModel(
    val tipoTransaccion: String,
    val etMonto: Double,
    val spCategoria: String,
    val etDescripcion: String,
    val etFecha: String
)
