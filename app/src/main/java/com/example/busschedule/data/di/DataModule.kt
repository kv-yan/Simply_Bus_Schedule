package com.example.busschedule.data.di

import com.example.busschedule.data.dao.BusScheduleDao
import com.example.busschedule.data.database.BusScheduleDatabase
import com.example.busschedule.data.repository.ScheduleRepositoryImpl
import com.example.busschedule.domain.repository.ScheduleRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single<BusScheduleDatabase> {
        BusScheduleDatabase.getDatabase(androidContext())
    }

    single<BusScheduleDao> { get<BusScheduleDatabase>().busScheduleDao() }


    singleOf(::ScheduleRepositoryImpl) { bind<ScheduleRepository>() }
}
