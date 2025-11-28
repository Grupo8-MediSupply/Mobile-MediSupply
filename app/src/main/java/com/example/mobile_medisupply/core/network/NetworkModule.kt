package com.example.mobile_medisupply.core.network

import com.example.mobile_medisupply.features.auth.data.local.SessionManager
import com.example.mobile_medisupply.features.auth.data.remote.AuthApi
import com.example.mobile_medisupply.features.clients.data.remote.ClienteApi
import com.example.mobile_medisupply.features.config.data.remote.ConfigApi
import com.example.mobile_medisupply.features.home.data.remote.VisitApi
import com.example.mobile_medisupply.features.orders.data.remote.ProductApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://medi-g8-b0kxqvrx.ue.gateway.dev/api/v1/"

    @Provides @Singleton fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val url = original.url.toString()

        // No incluir el token en login o registro de clientes (endpoints públicos)
        if (url.contains("login") || url.contains("clientes")) {
            return@Interceptor chain.proceed(original)
        }

        val token = sessionManager.getSession()?.token
        val newRequest =
                original.newBuilder()
                        .apply {
                            if (!token.isNullOrBlank()) {
                                addHeader("Authorization", "Bearer $token")
                            }
                        }
                        .build()

        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideConfigApi(retrofit: Retrofit): ConfigApi = retrofit.create(ConfigApi::class.java)

    @Provides
    @Singleton
    fun provideClientesApi(retrofit: Retrofit): ClienteApi = retrofit.create(ClienteApi::class.java)

    @Provides
    @Singleton
    fun provideVisitApi(retrofit: Retrofit): VisitApi = retrofit.create(VisitApi::class.java)

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)
}

data class ApiResponse<T>(
        val success: Boolean,
        val result: T? = null,
        val message: String? = null,
        val timestamp: String? = null
)
