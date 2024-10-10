package com.example.busschedule.presentation.di

import com.example.busschedule.presentation.viewmodel.BusScheduleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presenterModule = module {
    viewModelOf(::BusScheduleViewModel)
}