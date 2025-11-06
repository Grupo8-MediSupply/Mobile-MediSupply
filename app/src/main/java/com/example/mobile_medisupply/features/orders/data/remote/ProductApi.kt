package com.example.mobile_medisupply.features.orders.data.remote

import com.example.mobile_medisupply.core.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {

    @POST("ordenes/porVendedor/{clienteId}")
    suspend fun crearPedidoDesdeVendedor(
        @Path("clienteId") clienteId: String,
        @Body request: List<ProductItemRequest>
    ): ApiResponse<ApiResponse<ProductCreted>>

    @GET("producto/ObtenerProductos")
    suspend fun obtenerProductos(): ApiResponse<List<ProductDto>>

    @GET("producto/{productId}")
    suspend fun obtenerDetalleProducto(
        @Path("productId") productId: String
    ): ProductDetailResult
}

data class ProductCreted(
    val id: String,
    val estado: String
)

data class ProductItemRequest(
    val lote: String,
    val bodega: String,
    val cantidad: Int
)

data class ProductDto(
    val productoRegionalId: String,
    val sku: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int
)


data class ProductDetailResult(
    val producto_info: ProductInfo,
    val tipo: String,
    val precio: Double,
    val proveedor: Supplier,
    val productoPaisId: String,
    val bodegas: List<Warehouse>
)

data class ProductInfo(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val sku: String,
    val nombre: String,
    val descripcion: String,
    val concentracion: String
)

data class Supplier(
    val id: String,
    val nombre: String,
    val pais: String
)

data class Warehouse(
    val bodegaId: String,
    val bodegaNombre: String,
    val lotes: List<Batch>
)

data class Batch(
    val loteId: String,
    val cantidad: Int
)