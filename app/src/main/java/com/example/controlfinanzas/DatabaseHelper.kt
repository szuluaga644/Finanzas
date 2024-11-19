package com.example.controlfinanzas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "control_finanzas.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "transacciones"
        const val COL_ID = "id"
        const val COL_TIPO = "tipo"
        const val COL_MONTO = "monto"
        const val COL_CATEGORIA = "categoria"
        const val COL_DESCRIPCION = "descripcion"
        const val COL_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TIPO TEXT,
                $COL_MONTO REAL,
                $COL_CATEGORIA TEXT,
                $COL_DESCRIPCION TEXT,
                $COL_FECHA TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertarTransaccion(tipo: String, monto: Double, categoria: String, descripcion: String, fecha: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TIPO, tipo)
            put(COL_MONTO, monto)
            put(COL_CATEGORIA, categoria)
            put(COL_DESCRIPCION, descripcion)
            put(COL_FECHA, fecha)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun obtenerTransacciones(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_NAME, null, null, null, null, null, "$COL_FECHA DESC")
    }

    fun eliminarTransaccion(tipo: String?, monto: Double?, categoria: String?, descripcion: String?, fecha: String?): Int {
        val db = writableDatabase
        val whereClause = "$COL_TIPO = ? AND $COL_MONTO = ? AND $COL_CATEGORIA = ? AND $COL_DESCRIPCION = ? AND $COL_FECHA = ?"
        val whereArgs = arrayOf(tipo, monto.toString(), categoria, descripcion, fecha)
        return db.delete(TABLE_NAME, whereClause, whereArgs)
    }

}
