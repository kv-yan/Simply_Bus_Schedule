package com.example.busschedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.busschedule.domain.model.BusScheduleDomain
import com.example.busschedule.domain.usecase.GetAllBusScheduleUseCase
import com.example.busschedule.domain.usecase.GetBusScheduleByStopNameUseCase
import kotlinx.coroutines.flow.Flow

class BusScheduleViewModel(
    private val getAllBusScheduleUseCase: GetAllBusScheduleUseCase,
    private val getBusScheduleByStopNameUseCase: GetBusScheduleByStopNameUseCase,
) : ViewModel() {

    // Get example bus schedule
    fun getFullSchedule(): Flow<List<BusScheduleDomain>> = getAllBusScheduleUseCase.invoke()

    // Get example bus schedule by stop
    fun getScheduleFor(stopName: String): Flow<List<BusScheduleDomain>> =
        getBusScheduleByStopNameUseCase.invoke(stopName)
}
