package com.example.flypo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.example.flypo.app.Viaje
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_detalle_destino.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetalleDestino : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var viaje: Viaje
    private lateinit var dataBase: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_destino)
        dataBase = AppDataBase.getDatabase(this)

        if(intent.hasExtra("Viaje")){
            viaje = intent.extras?.getSerializable("Viaje") as Viaje
            llenarCard()
            createFragment(R.id.map)
        }
    }

    private fun llenarCard() {
        if(viaje !== null){
            originName.text = viaje.originName
            destineName.text = viaje.destineName
        }
    }

    private fun createFragment(id: Int) {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        createPolylines()
    }

    private fun createPolylines() {
        if(viaje !== null){
            val coordinatesDestine = LatLng(viaje.destineLat, viaje.destineLng)
            val coordinates = LatLng(viaje.originLat,viaje.originLng)

            val polylineOptions = PolylineOptions()
                .add(coordinates)
                .add(coordinatesDestine)
                .width(15f)
                .color(ContextCompat.getColor(this, R.color.purple_200))

            val polyline = map.addPolyline(polylineOptions)
            polyline.startCap = RoundCap()
            val pattern: List<PatternItem> = listOf(
                Dot(), Gap(10f), Dash(50f), Gap(10f)
            )
            polyline.pattern = pattern
        }
    }

    private fun createMarker() {
        if(viaje !== null){
            val coordinatesDestine = LatLng(viaje.destineLat, viaje.destineLng)
            val coordinates = LatLng(viaje.originLat,viaje.originLng)
            val marker: MarkerOptions = MarkerOptions().position(coordinates).title(viaje.originName)
            val markerDestine: MarkerOptions = MarkerOptions().position(coordinatesDestine).title(viaje.destineName)
            map.addMarker(marker)
            map.addMarker(markerDestine)
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordinatesDestine, 8f),
                4000,
                null
            )
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.edit_item->{
                val intent = Intent(this, NuevoViaje::class.java)
                intent.putExtra("Viaje",viaje)
                startActivity(intent)
            }

            R.id.delete_item->{
                CoroutineScope(Dispatchers.IO).launch {
                    dataBase.viaje().delete(viaje)
                    this@DetalleDestino.finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}