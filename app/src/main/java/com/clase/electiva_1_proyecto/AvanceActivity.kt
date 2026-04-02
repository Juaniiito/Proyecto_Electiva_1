package com.clase.electiva_1_proyecto


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AvanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avance)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}