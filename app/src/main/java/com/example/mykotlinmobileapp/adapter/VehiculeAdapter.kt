package com.example.mykotlinmobileapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mykotlinmobileapp.Entity.VehiculeItem
import com.example.mykotlinmobileapp.R


class VehiculeAdapter(var con: Context, private val listVehicules:ArrayList<VehiculeItem>) : RecyclerView.Adapter<VehiculeAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.RV_Image)
        val title : TextView = itemView.findViewById(R.id.RV_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicule_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehiculeAdapter.ViewHolder, position: Int) {

    Glide.with(con).load(listVehicules[position].imageVehicule).into(holder.image)//glide library pour l'image
        holder.title.text=listVehicules[position].modelVehicule
       /* Picasso.get().load(vehicules[position].photoUrl).into(holder.image)
        holder.title.text = vehicules[position].title*/
    }



    override fun getItemCount() = listVehicules.size
}