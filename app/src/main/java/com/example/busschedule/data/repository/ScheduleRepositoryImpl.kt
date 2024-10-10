package com.example.busschedule.data.repository

import com.example.busschedule.data.dao.BusScheduleDao
import com.example.busschedule.data.extensions.toDomain
import com.example.busschedule.domain.model.BusScheduleDomain
import com.example.busschedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl(private val busScheduleDao: BusScheduleDao) : ScheduleRepository {
    override fun getAllBusSchedules(): Flow<List<BusScheduleDomain>> {
        return busScheduleDao.getAllBusSchedules().map { list ->
            list.map { entity ->
                entity.toDomain()
            }
        }
    }


    override fun getSchedulesForStop(stopName: String): Flow<List<BusScheduleDomain>> =
        busScheduleDao.getSchedulesForStop(stopName).map { list ->
            list.map { entity ->
                entity.toDomain()
            }
        }
}


