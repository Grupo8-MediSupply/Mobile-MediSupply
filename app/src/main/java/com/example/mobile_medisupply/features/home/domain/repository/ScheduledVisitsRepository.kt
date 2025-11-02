package com.example.mobile_medisupply.features.home.domain.repository

import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit

interface ScheduledVisitsRepository {
    fun getScheduledVisits(): List<ScheduledVisit>
}
