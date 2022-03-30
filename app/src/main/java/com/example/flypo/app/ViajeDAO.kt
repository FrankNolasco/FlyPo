package com.example.flypo.app

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.flypo.app.Viaje

@Dao
interface ViajeDAO {
    @Query(value = "Select * from viaje")
    fun getAll(): LiveData<List<Viaje>>

    @Query(value = "Select * from viaje where idViaje=:id")
    fun get(id:Int):LiveData<Viaje>

    @Insert
    fun insertAll(vararg travel:Viaje)

    @Update
    fun update(travel: Viaje)

    @Delete
    fun delete(travel: Viaje)

}