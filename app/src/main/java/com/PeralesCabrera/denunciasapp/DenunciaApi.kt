package com.PeralesCabrera.denunciasapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/** Los 5 endpoints de la API denunciaservice. */
interface DenunciaApi {

    @POST("denuncias/")
    suspend fun registrar(@Body denuncia: DenunciaRequest): Denuncia

    @GET("denuncias/")
    suspend fun listar(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<Denuncia>

    @GET("denuncias/{id}")
    suspend fun obtener(@Path("id") id: Int): Denuncia

    @GET("denuncias/ciudadano/{dni}")
    suspend fun porCiudadano(@Path("dni") dni: String): List<Denuncia>

    @PUT("denuncias/{id}")
    suspend fun atender(@Path("id") id: Int): Denuncia
}

object RetrofitClient {

    // Emulador de Android Studio : http://10.0.2.2:8080/
    // Celular físico (misma red)  : http://IP_DE_TU_PC:8080/   (ej. http://192.168.1.20:8080/)
    // API publicada en Render     : https://denunciaservice-tuapellido.onrender.com/
    const val BASE_URL = "http://10.0.2.2:8080/"

    // Tiempos de espera largos: la primera petición a Render puede tardar cerca de un minuto.
    private val cliente = OkHttpClient.Builder()
        .connectTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .build()

    val api: DenunciaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DenunciaApi::class.java)
    }
}
