package com.example.busschedule.domain.usecase

import com.example.busschedule.domain.model.BusScheduleDomain
import com.example.busschedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

interface GetAllBusScheduleUseCase {
    fun invoke(): Flow<List<BusScheduleDomain>>
}


class GetAllBusScheduleUseCaseImpl(private val repository: ScheduleRepository) :
    GetAllBusScheduleUseCase {
    override fun invoke(): Flow<List<BusScheduleDomain>> {
        return repository.getAllBusSchedules()
    }
}
