package com.example.starwarsjunior

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

class MyApplication: Application() {

    companion object {
        private lateinit var sInstance: MyApplication
        //var database: AppDataBase? = null

        fun getAppContext(): Context {
            return sInstance.applicationContext
        }

        var userPermission = ""

        //val BASE_URL = "https://swapi.dev/api/"
        val BASE_URL = "https://swapi.py4e.com/api/"
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        AndroidThreeTen.init(this);
    }
}