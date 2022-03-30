package com.example.flypo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flypo.app.Viaje
import com.example.flypo.app.ViajeDAO

@Database(entities = [Viaje::class], version = 1)
abstract class AppDataBase:RoomDatabase() {
    abstract fun viaje():ViajeDAO
    companion object{
        @Volatile
        private var INSTANCE: AppDataBase?=null

        fun getDatabase(context: Context):AppDataBase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(lock =this ){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_viajes_1"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}