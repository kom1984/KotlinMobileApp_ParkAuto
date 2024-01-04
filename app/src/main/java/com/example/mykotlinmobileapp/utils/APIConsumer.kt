package com.example.mykotlinmobileapp.utils



import com.example.mykotlinmobileapp.Entity.VehiculeItem
import com.example.mykotlinmobileapp.data.AuthResponse
import com.example.mykotlinmobileapp.data.LoginBody
import com.example.mykotlinmobileapp.data.RegisterBody
import com.example.mykotlinmobileapp.data.UniqueEmailValidationResponse
import com.example.mykotlinmobileapp.data.ValidateEmailBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIConsumer {
    @POST("users/validate-unique-email")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBody):Response<UniqueEmailValidationResponse>

    @POST("api/auth/signup")
    suspend fun registerUser(@Body body: RegisterBody):Response<AuthResponse>

    @POST("api/auth/signin")
    suspend fun loginUser(@Body body: LoginBody):Response<AuthResponse>

    @GET("/vehicules")
    fun getVehiculesData(): Call<List<VehiculeItem>>
}

