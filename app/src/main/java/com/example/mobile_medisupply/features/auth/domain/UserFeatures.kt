package com.example.mobile_medisupply.features.auth.domain

import com.example.mobile_medisupply.features.auth.domain.model.UserRole

enum class Feature {
    VIEW_ORDERS,
    CREATE_ORDER,
    MANAGE_INVENTORY,
    MANAGE_USERS,
    VIEW_REPORTS,
    MANAGE_PRICES,
    VIEW_CATALOG,
    MANAGE_PROFILE
}

object UserFeatures {
    private val roleFeatures =
            mapOf(
                    UserRole.ADMIN to
                            setOf(
                                    Feature.VIEW_ORDERS,
                                    Feature.CREATE_ORDER,
                                    Feature.MANAGE_INVENTORY,
                                    Feature.MANAGE_USERS,
                                    Feature.VIEW_REPORTS,
                                    Feature.MANAGE_PRICES,
                                    Feature.VIEW_CATALOG,
                                    Feature.MANAGE_PROFILE
                            ),
                    UserRole.VENDEDOR to
                            setOf(
                                    Feature.VIEW_ORDERS,
                                    Feature.CREATE_ORDER,
                                    Feature.VIEW_CATALOG,
                                    Feature.VIEW_REPORTS,
                                    Feature.MANAGE_PROFILE
                            ),
                    UserRole.CLIENTE to
                            setOf(
                                    Feature.VIEW_ORDERS,
                                    Feature.CREATE_ORDER,
                                    Feature.VIEW_CATALOG,
                                    Feature.MANAGE_PROFILE
                            ),
                    UserRole.PROVEEDOR to
                            setOf(
                                    Feature.MANAGE_INVENTORY,
                                    Feature.VIEW_REPORTS,
                                    Feature.MANAGE_PROFILE
                            )
            )

    fun hasFeature(role: UserRole, feature: Feature): Boolean {
        return roleFeatures[role]?.contains(feature) ?: false
    }

    fun getFeaturesForRole(role: UserRole): Set<Feature> {
        return roleFeatures[role] ?: emptySet()
    }
}
