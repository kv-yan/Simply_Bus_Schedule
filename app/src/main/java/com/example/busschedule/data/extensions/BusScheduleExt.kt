package com.example.busschedule.data.extensions

import com.example.busschedule.data.model.entity.BusScheduleData
import com.example.busschedule.domain.model.BusScheduleDomain

fun BusScheduleData.toDomain(): BusScheduleDomain {
    return BusScheduleDomain(id, stopName, arrivalTimeInMillis)
}
