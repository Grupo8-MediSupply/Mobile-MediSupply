package com.example.mobile_medisupply.features.clients.data

import com.example.mobile_medisupply.features.clients.domain.model.ClientDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientOrderSummary
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitStatus
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitSummary
import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepository

class FakeClientRepository : ClientRepository {
    private val orders =
            listOf(
                    ClientOrderSummary(
                            id = "order-1",
                            title = "Orden 1",
                            description = "Supporting line text lorem ipsum dolor sit amet, consectetur."
                    ),
                    ClientOrderSummary(
                            id = "order-2",
                            title = "Orden 2",
                            description = "Supporting line text lorem ipsum dolor sit amet, consectetur."
                    ),
                    ClientOrderSummary(
                            id = "order-3",
                            title = "Orden 3",
                            description = "Supporting line text lorem ipsum dolor sit amet, consectetur."
                    ),
                    ClientOrderSummary(
                            id = "order-4",
                            title = "Orden 4",
                            description = "Supporting line text lorem ipsum dolor sit amet, consectetur."
                    ),
                    ClientOrderSummary(
                            id = "order-5",
                            title = "Orden 5",
                            description = "Supporting line text lorem ipsum dolor sit amet, consectetur."
                    )
            )

    private val clientLocations =
            mapOf(
                    "clinica-san-rafael" to "Bogotá",
                    "hospital-vida-plena" to "Medellín",
                    "centro-medico-los-alamos" to "Bucaramanga",
                    "salud-integral-norte" to "Barranquilla",
                    "hospital-angeles-del-valle" to "Cali",
                    "clinica-santa-lucia" to "Pereira",
                    "centro-medico-las-lomas" to "Cartagena",
                    "hospital-buen-vivir" to "Manizales",
                    "clinica-esperanza-azul" to "Villavicencio"
            )

    private val visitsByClient: Map<String, List<ClientVisitSummary>> =
            mapOf(
                    "clinica-san-rafael" to
                            listOf(
                                    ClientVisitSummary(
                                            id = "visit-1001",
                                            title = "Visita 1",
                                            scheduledDate = "05/20/2024",
                                            status = ClientVisitStatus.PROGRAMADA
                                    ),
                                    ClientVisitSummary(
                                            id = "visit-1002",
                                            title = "Visita 2",
                                            scheduledDate = "05/12/2024",
                                            status = ClientVisitStatus.EN_CURSO
                                    ),
                                    ClientVisitSummary(
                                            id = "4217f1a5-acca-4e50-81c7-50b9a11a3697",
                                            title = "Visita 3",
                                            scheduledDate = "04/19/2024",
                                            status = ClientVisitStatus.FINALIZADA
                                    )
                            ),
                    "hospital-vida-plena" to
                            listOf(
                                    ClientVisitSummary(
                                            id = "visit-2001",
                                            title = "Inspección quirófanos",
                                            scheduledDate = "05/18/2024",
                                            status = ClientVisitStatus.EN_CURSO
                                    ),
                                    ClientVisitSummary(
                                            id = "visit-2002",
                                            title = "Capacitación personal",
                                            scheduledDate = "04/30/2024",
                                            status = ClientVisitStatus.FINALIZADA
                                    )
                            ),
                    "centro-medico-los-alamos" to
                            listOf(
                                    ClientVisitSummary(
                                            id = "visit-3001",
                                            title = "Auditoría equipos",
                                            scheduledDate = "05/21/2024",
                                            status = ClientVisitStatus.PROGRAMADA
                                    )
                            ),
                    "salud-integral-norte" to
                            listOf(
                                    ClientVisitSummary(
                                            id = "visit-4001",
                                            title = "Seguimiento repuestos",
                                            scheduledDate = "05/10/2024",
                                            status = ClientVisitStatus.EN_CURSO
                                    )
                            ),
                    "hospital-angeles-del-valle" to
                            listOf(
                                    ClientVisitSummary(
                                            id = "visit-5001",
                                            title = "Evaluación quirófanos",
                                            scheduledDate = "04/28/2024",
                                            status = ClientVisitStatus.FINALIZADA
                                    )
                            ),
                    "clinica-santa-lucia" to emptyList(),
                    "centro-medico-las-lomas" to emptyList(),
                    "hospital-buen-vivir" to emptyList(),
                    "clinica-esperanza-azul" to emptyList()
            )

    private val visitDetails: Map<String, Map<String, ClientVisitDetail>> =
            visitsByClient.mapValues { (clientId, visits) ->
                val location = clientLocations[clientId] ?: "Colombia"
                visits.associateBy({ it.id }) { summary ->
                    ClientVisitDetail(
                            id = summary.id,
                            title = summary.title,
                            scheduledDate = summary.scheduledDate,
                            status = summary.status,
                            location = location,
                            notes =
                                    when (summary.status) {
                                        ClientVisitStatus.PROGRAMADA ->
                                                "Revisión de inventario y capacitación inicial."
                                        ClientVisitStatus.EN_CURSO ->
                                                "Instalación de nuevos equipos en área UCI."
                                        ClientVisitStatus.FINALIZADA ->
                                                "Se entregó reporte técnico al responsable."
                                    }
                    )
                }
            }

    private val clients: List<ClientDetail> =
            listOf(
                    ClientDetail(
                            id = "clinica-san-rafael",
                            name = "Clínica San Rafael",
                            city = "Bogotá",
                            phone = "601 4455 8899",
                            institutionType = "Hospital",
                            contact = "Laura Gómez",
                            orders = orders.take(5),
                            visits = visitsByClient["clinica-san-rafael"].orEmpty()
                    ),
                    ClientDetail(
                            id = "hospital-vida-plena",
                            name = "Hospital Vida Plena",
                            city = "Medellín",
                            phone = "604 7788 2211",
                            institutionType = "Hospital",
                            contact = "Carlos Martínez",
                            orders = orders.take(4),
                            visits = visitsByClient["hospital-vida-plena"].orEmpty()
                    ),
                    ClientDetail(
                            id = "centro-medico-los-alamos",
                            name = "Centro Médico Los Álamos",
                            city = "Bucaramanga",
                            phone = "607 300 1122",
                            institutionType = "Clínica",
                            contact = "Ana Torres",
                            orders = orders,
                            visits = visitsByClient["centro-medico-los-alamos"].orEmpty()
                    ),
                    ClientDetail(
                            id = "salud-integral-norte",
                            name = "Salud Integral Norte",
                            city = "Barranquilla",
                            phone = "605 220 8899",
                            institutionType = "Hospital",
                            contact = "Ricardo Pérez",
                            orders = orders.take(3),
                            visits = visitsByClient["salud-integral-norte"].orEmpty()
                    ),
                    ClientDetail(
                            id = "hospital-angeles-del-valle",
                            name = "Hospital Ángeles del Valle",
                            city = "Cali",
                            phone = "602 3344 5566",
                            institutionType = "Hospital",
                            contact = "María Sánchez",
                            orders = orders.take(5),
                            visits = visitsByClient["hospital-angeles-del-valle"].orEmpty()
                    ),
                    ClientDetail(
                            id = "clinica-santa-lucia",
                            name = "Clínica Santa Lucía",
                            city = "Pereira",
                            phone = "606 129 7744",
                            institutionType = "Clínica",
                            contact = "Javier López",
                            orders = orders.take(2),
                            visits = emptyList()
                    ),
                    ClientDetail(
                            id = "centro-medico-las-lomas",
                            name = "Centro Médico Las Lomas",
                            city = "Cartagena",
                            phone = "605 883 5511",
                            institutionType = "Clínica",
                            contact = "Paula Hernández",
                            orders = orders.take(5),
                            visits = emptyList()
                    ),
                    ClientDetail(
                            id = "hospital-buen-vivir",
                            name = "Hospital Buen Vivir",
                            city = "Manizales",
                            phone = "606 330 7788",
                            institutionType = "Hospital",
                            contact = "Andrés Silva",
                            orders = orders.take(4),
                            visits = emptyList()
                    ),
                    ClientDetail(
                            id = "clinica-esperanza-azul",
                            name = "Clínica Esperanza Azul",
                            city = "Villavicencio",
                            phone = "608 665 1122",
                            institutionType = "Clínica",
                            contact = "Sofía Díaz",
                            orders = orders.take(3),
                            visits = emptyList()
                    )
            )

    override fun getClients(): List<ClientSummary> =
            clients.map {
                ClientSummary(
                        id = it.id,
                        name = it.name,
                        contact = it.contact,
                        location = it.city,
                        phone = it.phone,
                        institutionType = it.institutionType
                )
            }

    override fun getClientDetail(clientId: String): ClientDetail? =
            clients.find { it.id == clientId }

    override fun getClientVisitDetail(clientId: String, visitId: String): ClientVisitDetail? =
            visitDetails[clientId]?.get(visitId)
}
