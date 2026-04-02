package com.clase.electiva_1_proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los CheckBox
        val checkDesayuno = findViewById<CheckBox>(R.id.checkDesayuno)
        val checkPresentacion = findViewById<CheckBox>(R.id.checkPresentacion)
        val checkParcial = findViewById<CheckBox>(R.id.checkParcial)
        val checkVeterinario = findViewById<CheckBox>(R.id.checkVeterinario)

        // Botón Agregar Tarea
        val btnAgregarTarea = findViewById<Button>(R.id.btnAgregarTarea)
        btnAgregarTarea.setOnClickListener {
            Toast.makeText(this, "Funcionalidad: Agregar nueva tarea (próximamente)", Toast.LENGTH_SHORT).show()
        }

        // Botón Siguiente - Navega a la segunda pantalla
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        btnSiguiente.setOnClickListener {
            val intent = Intent(this, AvanceActivity::class.java)
            startActivity(intent)
        }

        // Listeners para los CheckBox (marcar/desmarcar tareas)
        checkDesayuno.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "✓ Desayuno completado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Desayuno pendiente", Toast.LENGTH_SHORT).show()
            }
        }

        checkPresentacion.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "✓ Presentación completada", Toast.LENGTH_SHORT).show()
            }
        }

        checkParcial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "✓ Parcial completado", Toast.LENGTH_SHORT).show()
            }
        }

        checkVeterinario.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "✓ Cita veterinario completada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}