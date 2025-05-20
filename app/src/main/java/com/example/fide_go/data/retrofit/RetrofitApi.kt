package com.example.fide_go.data.retrofit

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fide_go.data.retrofit.adapters.LocalDateAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
object RetrofitApi {
    private val  BASE_URL = "https://dkhmzp0m-8000.uks1.devtunnels.ms/" //IMPORTANTE CAMBIAR DEPENDIENDO DEL WIFI O RED

    // Configurar OkHttpClient con tiempos de espera personalizados
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Tiempo de espera de conexión
        .readTimeout(30, TimeUnit.SECONDS)     // Tiempo de espera de lectura
        .writeTimeout(30, TimeUnit.SECONDS)    // Tiempo de espera de escritura
        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()


    @RequiresApi(Build.VERSION_CODES.O)
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()




    val userService : UserService by lazy { retrofit.create(UserService::class.java) }
    val phoneService : PhoneService by lazy { retrofit.create(PhoneService::class.java) }
    val emailService : EmailService by lazy { retrofit.create(EmailService::class.java) }
    val profileService : ProfileService by lazy { retrofit.create(ProfileService::class.java) }
    val bussinessService : BussinessService by lazy { retrofit.create(BussinessService::class.java) }
    val offersService : OffersService by lazy { retrofit.create(OffersService::class.java) }



}
