package com.example.flypo.app

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Viaje")
class Viaje (
    val originName: String,
    val destineName: String,
    val originLat: Double,
    val originLng: Double,
    val destineLat: Double,
    val destineLng: Double,
    val price: Double,
    val date: String,
    val rememberPhoto: String,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    var idViaje: Int = 0
): Serializable