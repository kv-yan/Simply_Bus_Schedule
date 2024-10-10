package com.example.busschedule.domain.di

import com.example.busschedule.domain.usecase.GetBusScheduleByStopNameUseCase
import com.example.busschedule.domain.usecase.GetBusScheduleByStopNameUseCaseImpl
import com.example.busschedule.domain.usecase.GetAllBusScheduleUseCase
import com.example.busschedule.domain.usecase.GetAllBusScheduleUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetAllBusScheduleUseCaseImpl) { bind<GetAllBusScheduleUseCase>() }
    factoryOf(::GetBusScheduleByStopNameUseCaseImpl) { bind<GetBusScheduleByStopNameUseCase>() }

}