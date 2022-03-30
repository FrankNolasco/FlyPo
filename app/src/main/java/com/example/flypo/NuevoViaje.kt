package com.example.flypo
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.flypo.app.Viaje
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_nuevo_viaje.*
import java.lang.Double
import java.text.NumberFormat
import kotlin.Error
import kotlin.Int
import kotlin.Suppress

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class NuevoViaje : AppCompatActivity() {

    var placesClient: PlacesClient? = null
    var _idViaje: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_viaje)
        val ctx = this
        inicializeClient(ctx)
        inicializeAutocomplete(R.id.place_autocomplete_fragment, originName, Lat, Lng)
        inicializeAutocomplete(R.id.destine_selector, destineName, destineLat, destineLng)

        if (intent.hasExtra("Viaje")){
            val editViaje = intent.extras?.getSerializable("Viaje") as Viaje
            llenarCard(originName, editViaje.originName, Lat, editViaje.originLat.toString(), Lng, editViaje.originLng.toString() )
            llenarCard(destineName, editViaje.destineName, destineLat, editViaje.destineLat.toString(), destineLng, editViaje.destineLng.toString())
            _idViaje = editViaje.idViaje
            val mdate = editViaje.date
            val sp = mdate.split('/')
            if(sp.size === 3){
                datePicker1.init(sp.get(2).toInt(),sp.get(1).toInt(), sp.get(0).toInt(), null);
            }
        }


        button2.setOnClickListener {
            val _viaje = getNewTravel()
            if(_viaje !== null){
                if(_idViaje === 0){
                    agregarViaje(ctx, _viaje)
                }else{
                    editarViaje(ctx, _viaje)
                }

            }
        }
    }

    fun editarViaje(ctx: Context, viaje: Viaje){
        CoroutineScope(Dispatchers.IO).launch {
            viaje.idViaje = _idViaje
            val dataBase = AppDataBase.getDatabase(ctx)
            dataBase.viaje().update(viaje)
            this@NuevoViaje.finish()
        }
    }

    fun agregarViaje(ctx: Context, viaje: Viaje) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = AppDataBase.getDatabase(ctx)
            dataBase.viaje().insertAll(viaje)
            this@NuevoViaje.finish()
        }
    }

    fun inicializeClient(ctx: Context) {
        val apiKey = getString(R.string.google_maps_key)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        placesClient = Places.createClient(ctx)
    }

    fun inicializeAutocomplete(idFragment: Int, name: TextView, lat: TextView, lng: TextView) {
        val autocompleteFragment = supportFragmentManager.findFragmentById(idFragment) as AutocompleteSupportFragment?
        autocompleteFragment!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) { onPlaceSelected(place, name, lat, lng) }
            override fun onError(status: Status) {
                Toast.makeText(applicationContext, status.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onPlaceSelected(place: Place, _name: TextView, _Lat: TextView, _Lng: TextView){
        val address = place.address.toString()
        val lat = place.latLng?.latitude!!
        val long = place.latLng?.longitude!!
        llenarCard(_name, address, _Lat, lat.toString(), _Lng, long.toString())
        //Picasso.get().load(place.websiteUri).into(_image)
    }

    fun llenarCard(_name: TextView,address: String, _Lat: TextView, lat: String, _Lng: TextView, lng: String) {
        _name.text = address
        _Lat.text = lat
        _Lng.text = lng
        calcularPrecio()
    }

    private fun calcularPrecio(){
        try {
            val lat1 = Double.parseDouble(Lat.text.toString())
            val lng1 = Double.parseDouble(Lng.text.toString())
            val lat2 = Double.parseDouble(destineLat.text.toString())
            val lng2 = Double.parseDouble(destineLng.text.toString())
            if(!(lat1 === 0.0 || lat2 === 0.0 || lng1 === 0.0 || lng2 === 0.0)){
                val location = Location("location 1")
                val location2 = Location("location 2")
                location.latitude = lat1
                location.longitude = lng1
                location2.latitude = lat2
                location2.longitude = lng2
                val distance = location.distanceTo(location2)
                val nf: NumberFormat = NumberFormat.getInstance()
                nf.setMinimumFractionDigits(0)
                nf.setMaximumFractionDigits(2)
                 precio.text = (nf.format(distance * .00005)).toString()
            }
        }catch (error: Error) {
        }
    }

    private fun getNewTravel() : Viaje?{
        val day = datePicker1.dayOfMonth
        val month = datePicker1.month + 1
        val year = datePicker1.year
        val lat1 = Double.parseDouble(Lat.text.toString())
        val lng1 = Double.parseDouble(Lng.text.toString())
        val lat2 = Double.parseDouble(destineLat.text.toString())
        val lng2 = Double.parseDouble(destineLng.text.toString())
        val price = Double.parseDouble(precio.text.toString())
        if(!(lat1 === 0.0 || lat2 === 0.0 || lng1 === 0.0 || lng2 === 0.0)){
            return Viaje(
                originName = originName.text.toString(),
                originLat = lat1,
                originLng = lng1,
                destineName = destineName.text.toString(),
                destineLat = lat2,
                destineLng = lng2,
                price = price,
                date = "${day}/${month}/${year}",
                rememberPhoto = "",
                description = ""
            )
        }else{
            Toast.makeText(applicationContext, "No se puede crear un viaje sin origen ni destino", Toast.LENGTH_SHORT).show()
            return null
        }
    }
}