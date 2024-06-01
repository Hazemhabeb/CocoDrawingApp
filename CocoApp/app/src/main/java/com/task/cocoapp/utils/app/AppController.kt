package com.task.cocoapp.utils.app

import android.app.Application
import android.util.Log

class AppController : Application() {


    companion object {
        lateinit var instance: AppController
            private set
    }


    // Called when the application is starting, before any other application objects have been created.
    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        instance = this
    }
    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }



}