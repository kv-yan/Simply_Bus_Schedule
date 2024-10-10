package com.example.busschedule.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.busschedule.data.model.entity.BusScheduleData
import kotlinx.coroutines.flow.Flow

@Dao
interface BusScheduleDao {

    @Query("SELECT * FROM Schedule ORDER BY arrival_time ASC")
    fun getAllBusSchedules(): Flow<List<BusScheduleData>>

    @Query("SELECT * FROM Schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
    fun getSchedulesForStop(stopName: String): Flow<List<BusScheduleData>>
}
