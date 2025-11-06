package com.example.mobile_medisupply.features.home.data

import com.example.mobile_medisupply.features.home.data.repository.ScheduledVisitRepositoryImp
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule{

    @Binds
    @Singleton
    abstract fun bindScheduleVisitsRepository(
        impl: ScheduledVisitRepositoryImp
    ) : ScheduledVisitsRepositoryImp

}