package com.example.mobile_medisupply.features.clients.data

import com.example.mobile_medisupply.features.clients.domain.model.ClientDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientOrderSummary
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
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

    private val clients: List<ClientDetail> =
            listOf(
                    ClientDetail(
                            id = "clinica-san-rafael",
                            name = "Clínica San Rafael",
                            city = "Ciudad de México",
                            phone = "55 4455 8899",
                            institutionType = "Hospital",
                            contact = "Laura Gómez",
                            orders = orders.take(5)
                    ),
                    ClientDetail(
                            id = "hospital-vida-plena",
                            name = "Hospital Vida Plena",
                            city = "Guadalajara",
                            phone = "33 7788 2211",
                            institutionType = "Hospital",
                            contact = "Carlos Martínez",
                            orders = orders.take(4)
                    ),
                    ClientDetail(
                            id = "centro-medico-los-alamos",
                            name = "Centro Médico Los Álamos",
                            city = "Bogotá",
                            phone = "601 300 1122",
                            institutionType = "Clínica",
                            contact = "Ana Torres",
                            orders = orders
                    ),
                    ClientDetail(
                            id = "salud-integral-norte",
                            name = "Salud Integral Norte",
                            city = "Medellín",
                            phone = "604 220 8899",
                            institutionType = "Hospital",
                            contact = "Ricardo Pérez",
                            orders = orders.take(3)
                    ),
                    ClientDetail(
                            id = "hospital-angeles-del-valle",
                            name = "Hospital Ángeles del Valle",
                            city = "Monterrey",
                            phone = "81 3344 5566",
                            institutionType = "Hospital",
                            contact = "María Sánchez",
                            orders = orders.take(5)
                    ),
                    ClientDetail(
                            id = "clinica-santa-lucia",
                            name = "Clínica Santa Lucía",
                            city = "Querétaro",
                            phone = "442 129 7744",
                            institutionType = "Clínica",
                            contact = "Javier López",
                            orders = orders.take(2)
                    ),
                    ClientDetail(
                            id = "centro-medico-las-lomas",
                            name = "Centro Médico Las Lomas",
                            city = "Puebla",
                            phone = "222 883 5511",
                            institutionType = "Clínica",
                            contact = "Paula Hernández",
                            orders = orders.take(5)
                    ),
                    ClientDetail(
                            id = "hospital-buen-vivir",
                            name = "Hospital Buen Vivir",
                            city = "Cali",
                            phone = "602 330 7788",
                            institutionType = "Hospital",
                            contact = "Andrés Silva",
                            orders = orders.take(4)
                    ),
                    ClientDetail(
                            id = "clinica-esperanza-azul",
                            name = "Clínica Esperanza Azul",
                            city = "Toluca",
                            phone = "722 665 1122",
                            institutionType = "Clínica",
                            contact = "Sofía Díaz",
                            orders = orders.take(3)
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
}
