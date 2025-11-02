package com.example.mobile_medisupply.features.orders.data

import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductInventory
import com.example.mobile_medisupply.features.orders.domain.model.ProductPricing
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepository

class FakeProductCatalogRepository : ProductCatalogRepository {

    private val products =
            listOf(
                    ProductCatalogItem(
                            id = "amoxicilina-500",
                            name = "Amoxicilina 500mg",
                            description =
                                    "Antibiótico betalactámico en cápsulas de 500 mg, caja con 20 unidades.",
                            presentation = "Cápsulas 500 mg - Caja 20",
                            unit = "Caja",
                            category = "Antibióticos",
                            provider = "FarmaLatam",
                            country = "CO",
                            guidelineLabel = "Normativa",
                            inventory =
                                    ProductInventory(
                                            total = 320,
                                            reserved = 40,
                                            available = 280,
                                            warehouse = "Centro Norte"
                                    ),
                            pricing =
                                    ProductPricing(
                                            currency = "COP",
                                            price = 12500.0,
                                            formattedPrice = "$ 12.500 COP",
                                            lastUpdated = "10/02/2025"
                                    )
                    ),
                    ProductCatalogItem(
                            id = "paracetamol-1g",
                            name = "Paracetamol 1 g",
                            description = "Tabletas analgésicas y antipiréticas de liberación inmediata.",
                            presentation = "Tableta 1 g - Blister 10",
                            unit = "Blister",
                            category = "Analgesia",
                            provider = "Salud Pharma",
                            country = "MX",
                            guidelineLabel = null,
                            inventory =
                                    ProductInventory(
                                            total = 540,
                                            reserved = 60,
                                            available = 480,
                                            warehouse = "CDMX Centro"
                                    ),
                            pricing =
                                    ProductPricing(
                                            currency = "MXN",
                                            price = 95.0,
                                            formattedPrice = "$ 95.00 MXN",
                                            lastUpdated = "05/01/2025"
                                    )
                    ),
                    ProductCatalogItem(
                            id = "solucion-salina-09",
                            name = "Solución Salina 0.9%",
                            description = "Bolsa de solución salina estéril al 0.9% para uso IV.",
                            presentation = "Bolsa 500 ml",
                            unit = "Bolsa",
                            category = "Soluciones IV",
                            provider = "Hospitech",
                            country = "CO",
                            guidelineLabel = "Ficha técnica",
                            inventory =
                                    ProductInventory(
                                            total = 620,
                                            reserved = 120,
                                            available = 500,
                                            warehouse = "Centro Médico"
                                    ),
                            pricing =
                                    ProductPricing(
                                            currency = "COP",
                                            price = 8200.0,
                                            formattedPrice = "$ 8.200 COP",
                                            lastUpdated = "18/12/2024"
                                    )
                    ),
                    ProductCatalogItem(
                            id = "jeringa-5ml",
                            name = "Jeringa 5 ml",
                            description = "Jeringa estéril con aguja 21G, empaque individual.",
                            presentation = "Jeringa 5 ml",
                            unit = "Unidad",
                            category = "Material de curación",
                            provider = "MediTools",
                            country = "MX",
                            guidelineLabel = null,
                            inventory =
                                    ProductInventory(
                                            total = 920,
                                            reserved = 150,
                                            available = 770,
                                            warehouse = "Guadalajara Norte"
                                    ),
                            pricing =
                                    ProductPricing(
                                            currency = "MXN",
                                            price = 12.5,
                                            formattedPrice = "$ 12.50 MXN",
                                            lastUpdated = "22/11/2024"
                                    )
                    ),
                    ProductCatalogItem(
                            id = "guantes-nitrilo-m",
                            name = "Guantes de Nitrilo M",
                            description = "Caja con 100 guantes de nitrilo, libres de látex, talla mediana.",
                            presentation = "Caja x 100 unidades",
                            unit = "Caja",
                            category = "Material de curación",
                            provider = "SafeHands",
                            country = "CO",
                            guidelineLabel = "Certificación",
                            inventory =
                                    ProductInventory(
                                            total = 410,
                                            reserved = 70,
                                            available = 340,
                                            warehouse = "Monterrey Centro"
                                    ),
                            pricing =
                                    ProductPricing(
                                            currency = "COP",
                                            price = 43500.0,
                                            formattedPrice = "$ 43.500 COP",
                                            lastUpdated = "08/01/2025"
                                    )
                    )
            )

    override fun getCatalog(): List<ProductCatalogItem> = products
}
