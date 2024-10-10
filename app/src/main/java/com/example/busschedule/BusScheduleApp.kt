package com.example.busschedule

import android.app.Application
import com.example.busschedule.data.di.dataModule
import com.example.busschedule.domain.di.domainModule
import com.example.busschedule.presentation.di.presenterModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BusScheduleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BusScheduleApp)
            modules(dataModule, domainModule, presenterModule)
        }
    }
}