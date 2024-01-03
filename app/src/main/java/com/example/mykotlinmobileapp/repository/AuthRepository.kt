package com.example.mykotlinmobileapp.repository

import com.example.mykotlinmobileapp.data.LoginBody
import com.example.mykotlinmobileapp.data.RegisterBody
import com.example.mykotlinmobileapp.data.UniqueEmailValidationResponse
import com.example.mykotlinmobileapp.data.ValidateEmailBody
import com.example.mykotlinmobileapp.utils.APIConsumer
import com.example.mykotlinmobileapp.utils.RequestStatus
import com.example.mykotlinmobileapp.utils.SimplifiedMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AuthRepository(val consumer:APIConsumer) {

    fun registerUser(body: RegisterBody)= flow {
        emit(RequestStatus.Waiting)
        val response = consumer.registerUser(body)

        if(response.isSuccessful){
            emit((RequestStatus.Success(response.body()!!)))
        }else{
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(response.errorBody()!!.byteStream()
                        .reader().readText())
                )
            )
        }
    }
    fun loginUser(body: LoginBody)= flow {
        emit(RequestStatus.Waiting)
        val response = consumer.loginUser(body)

        if(response.isSuccessful){
            emit((RequestStatus.Success(response.body()!!)))
        }else{
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(response.errorBody()!!.byteStream()
                        .reader().readText())
                )
            )
        }
    }

    fun validateEmailAddress(body: ValidateEmailBody): Flow<RequestStatus<UniqueEmailValidationResponse>> =
        flow{
            emit(RequestStatus.Waiting)
            val response = consumer.validateEmailAddress(body)

            if(response.isSuccessful){
                emit((RequestStatus.Success(response.body()!!)))
            }else{
                emit(
                    RequestStatus.Error(
                        SimplifiedMessage.get(response.errorBody()!!.byteStream()
                            .reader().readText())
                    )
                )
            }

        }
}