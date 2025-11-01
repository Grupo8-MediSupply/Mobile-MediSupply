package com.example.mobile_medisupply.features.orders.data

import com.example.mobile_medisupply.features.orders.domain.model.ProductAvailability
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductLot
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepository

class FakeProductCatalogRepository : ProductCatalogRepository {

    private val products =
            listOf(
                    ProductCatalogItem(
                            id = "amoxicilina-500",
                            name = "Amoxicilina 500mg",
                            category = "Antibióticos",
                            description =
                                    "Antibiótico de amplio espectro en cápsulas de 500mg.",
                            availability =
                                    listOf(
                                            ProductAvailability(
                                                    warehouseId = "cdmx-centro",
                                                    warehouseName = "CDMX - Centro",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "amox-500-cdmx-1",
                                                                            lotCode = "L23045",
                                                                            expiresAt = "Oct 2025",
                                                                            availableUnits = 120
                                                                    ),
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "amox-500-cdmx-2",
                                                                            lotCode = "L23110",
                                                                            expiresAt = "Dic 2025",
                                                                            availableUnits = 80
                                                                    )
                                                            )
                                            ),
                                            ProductAvailability(
                                                    warehouseId = "gdl-norte",
                                                    warehouseName = "Guadalajara - Norte",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "amox-500-gdl-1",
                                                                            lotCode = "L23090",
                                                                            expiresAt = "Nov 2025",
                                                                            availableUnits = 65
                                                                    )
                                                            )
                                            )
                                    )
                    ),
                    ProductCatalogItem(
                            id = "paracetamol-1g",
                            name = "Paracetamol 1g",
                            category = "Analgesia",
                            description = "Analgésico y antipirético en tabletas de 1g.",
                            availability =
                                    listOf(
                                            ProductAvailability(
                                                    warehouseId = "mty-centro",
                                                    warehouseName = "Monterrey - Centro",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "para-1g-mty-1",
                                                                            lotCode = "P23120",
                                                                            expiresAt = "Sep 2025",
                                                                            availableUnits = 210
                                                                    )
                                                            )
                                            ),
                                            ProductAvailability(
                                                    warehouseId = "cdmx-centro",
                                                    warehouseName = "CDMX - Centro",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "para-1g-cdmx-1",
                                                                            lotCode = "P23105",
                                                                            expiresAt = "Jul 2025",
                                                                            availableUnits = 150
                                                                    )
                                                            )
                                            )
                                    )
                    ),
                    ProductCatalogItem(
                            id = "solucion-salina-09",
                            name = "Solución Salina 0.9%",
                            category = "Soluciones Intravenosas",
                            description =
                                    "Bolsa de solución salina al 0.9% para administración intravenosa.",
                            availability =
                                    listOf(
                                            ProductAvailability(
                                                    warehouseId = "bogota-centro",
                                                    warehouseName = "Bogotá - Centro",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "salina-bog-1",
                                                                            lotCode = "S23088",
                                                                            expiresAt = "Ago 2025",
                                                                            availableUnits = 340
                                                                    ),
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "salina-bog-2",
                                                                            lotCode = "S23150",
                                                                            expiresAt = "Feb 2026",
                                                                            availableUnits = 190
                                                                    )
                                                            )
                                            )
                                    )
                    ),
                    ProductCatalogItem(
                            id = "jeringa-5ml",
                            name = "Jeringa 5ml",
                            category = "Material de Curación",
                            description = "Jeringa estéril de 5ml con aguja 21G.",
                            availability =
                                    listOf(
                                            ProductAvailability(
                                                    warehouseId = "cdmx-centro",
                                                    warehouseName = "CDMX - Centro",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "jeringa-cdmx-1",
                                                                            lotCode = "J23045",
                                                                            expiresAt = "Dic 2027",
                                                                            availableUnits = 500
                                                                    )
                                                            )
                                            ),
                                            ProductAvailability(
                                                    warehouseId = "gdl-norte",
                                                    warehouseName = "Guadalajara - Norte",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "jeringa-gdl-1",
                                                                            lotCode = "J23088",
                                                                            expiresAt = "Oct 2027",
                                                                            availableUnits = 320
                                                                    )
                                                            )
                                            )
                                    )
                    ),
                    ProductCatalogItem(
                            id = "guantes-nitrilo-m",
                            name = "Guantes de Nitrilo M",
                            category = "Material de Curación",
                            description =
                                    "Caja con 100 guantes de nitrilo talla mediana, libre de látex.",
                            availability =
                                    listOf(
                                            ProductAvailability(
                                                    warehouseId = "mty-centro",
                                                    warehouseName = "Monterrey - Centro",
                                                    lots =
                                                            listOf(
                                                                    ProductLot(
                                                                            lotId =
                                                                                    "guantes-mty-1",
                                                                            lotCode = "G23100",
                                                                            expiresAt = "May 2026",
                                                                            availableUnits = 420
                                                                    )
                                                            )
                                            )
                                    )
                    )
            )

    override fun getCatalog(): List<ProductCatalogItem> = products
}
