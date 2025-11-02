package com.example.mobile_medisupply.features.home.data

import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepository

object ScheduledVisitsRepositoryProvider {
    val repository: ScheduledVisitsRepository by lazy { FakeScheduledVisitsRepository() }
}
