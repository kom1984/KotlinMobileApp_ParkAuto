package com.example.mykotlinmobileapp.data

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("_id") val id:String,val firstName:String,val lastName:String,val email:String,val password:String)
