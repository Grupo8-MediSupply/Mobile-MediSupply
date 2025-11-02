package com.example.mobile_medisupply.features.home.data

import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepository
class FakeScheduledVisitsRepository : ScheduledVisitsRepository {

    private val visits =
            listOf(
                    ScheduledVisit(
                            id = "visit-1001",
                            clientId = "clinica-san-rafael",
                            clientName = "Clínica San Rafael",
                            scheduledDate = "05/20/2024",
                            location = "Ciudad de México"
                    ),
                    ScheduledVisit(
                            id = "visit-1002",
                            clientId = "hospital-vida-plena",
                            clientName = "Hospital Vida Plena",
                            scheduledDate = "05/22/2024",
                            location = "Guadalajara"
                    ),
                    ScheduledVisit(
                            id = "visit-1003",
                            clientId = "centro-medico-los-alamos",
                            clientName = "Centro Médico Los Álamos",
                            scheduledDate = "05/23/2024",
                            location = "Bogotá"
                    ),
                    ScheduledVisit(
                            id = "visit-1004",
                            clientId = "salud-integral-norte",
                            clientName = "Salud Integral Norte",
                            scheduledDate = "05/25/2024",
                            location = "Medellín"
                    ),
                    ScheduledVisit(
                            id = "visit-1005",
                            clientId = "hospital-angeles-del-valle",
                            clientName = "Hospital Ángeles del Valle",
                            scheduledDate = "05/27/2024",
                            location = "Monterrey"
                    ),
                    ScheduledVisit(
                            id = "visit-1006",
                            clientId = "clinica-santa-lucia",
                            clientName = "Clínica Santa Lucía",
                            scheduledDate = "05/28/2024",
                            location = "Querétaro"
                    ),
                    ScheduledVisit(
                            id = "visit-1007",
                            clientId = "hospital-buen-vivir",
                            clientName = "Hospital Buen Vivir",
                            scheduledDate = "05/29/2024",
                            location = "Cali"
                    )
            )

    override fun getScheduledVisits(): List<ScheduledVisit> = visits
}
