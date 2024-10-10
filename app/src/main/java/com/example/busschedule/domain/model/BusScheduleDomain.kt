package com.example.busschedule.domain.model

data class BusScheduleDomain(
    val id: Int,
    val stopName: String,
    val arrivalTimeInMillis: Int
)
