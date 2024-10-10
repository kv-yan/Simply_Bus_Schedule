package com.example.busschedule.domain.usecase

import com.example.busschedule.domain.model.BusScheduleDomain
import com.example.busschedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

interface GetBusScheduleByStopNameUseCase {
    fun invoke(stopName: String): Flow<List<BusScheduleDomain>>
}

class GetBusScheduleByStopNameUseCaseImpl(private val repository: ScheduleRepository) :
    GetBusScheduleByStopNameUseCase {
    override fun invoke(stopName: String): Flow<List<BusScheduleDomain>> {
        return repository.getSchedulesForStop(stopName)
    }
}