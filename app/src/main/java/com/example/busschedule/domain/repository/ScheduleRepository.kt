package com.example.busschedule.domain.repository

import com.example.busschedule.domain.model.BusScheduleDomain
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getAllBusSchedules(): Flow<List<BusScheduleDomain>>

    fun getSchedulesForStop(stopName: String): Flow<List<BusScheduleDomain>>
}