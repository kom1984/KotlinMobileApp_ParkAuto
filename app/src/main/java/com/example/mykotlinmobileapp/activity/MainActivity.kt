package com.example.mykotlinmobileapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinmobileapp.Entity.VehiculeItem
import com.example.mykotlinmobileapp.R
import com.example.mykotlinmobileapp.adapter.VehiculeAdapter
import com.example.mykotlinmobileapp.utils.APIConsumer

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: VehiculeAdapter
    var BASE_URL = "http://192.168.1.48:8080"
    // private lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /* val vehicules = arrayListOf<Vehicule_old>()
        for(i in 0..10){
            vehicules.add(Vehicule_old("BMW Serie 1","https://placehold.co/200x200/png",12000.00))
        }*/

        recyclerView  = findViewById(R.id.recycler_view)
        getAllData()
        recyclerView.layoutManager = LinearLayoutManager(this)



    }

    private fun getAllData() {
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIConsumer::class.java)

        var retroData = retrofit.getVehiculesData()

        retroData.enqueue(object : Callback<List<VehiculeItem>> {
            override fun onResponse(
                call: Call<List<VehiculeItem>>,
                response: Response<List<VehiculeItem>>
            ) {
                var data = response.body()!!

                myAdapter = VehiculeAdapter(baseContext, data as ArrayList<VehiculeItem>)
                recyclerView.adapter = myAdapter

                Log.d("data", data.toString())
            }

            override fun onFailure(call: Call<List<VehiculeItem>>, t: Throwable) {

            }

        })
    }


}