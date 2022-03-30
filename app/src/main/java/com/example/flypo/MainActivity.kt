package com.example.flypo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flypo.app.ViajeAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataBase = AppDataBase.getDatabase(this)
        dataBase.viaje().getAll().observe(this, Observer {
            GridLayoutManager( this,1 ).apply{ lista.layoutManager = this }
            lista.adapter = ViajeAdapter(it)
        })

        ButtonNew.setOnClickListener{
            val intent = Intent(this,NuevoViaje::class.java)
            startActivity(intent)
        }
    }
}