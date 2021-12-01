package com.unaj.loginsqlite.Adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.unaj.loginsqlite.DetailComplex
import com.unaj.loginsqlite.MenuActivity
import com.unaj.loginsqlite.R

import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.ui.home.HomeFragment


class ComplexAdapter (val complexs:List<Complex>): RecyclerView.Adapter<ComplexAdapter.ComplexHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplexHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ComplexHolder(layoutInflater.inflate(R.layout.item_complex, parent, false))
    }

    override fun onBindViewHolder(holder: ComplexHolder, position: Int) {
        holder.render(complexs[position])
    }

    override fun getItemCount(): Int {
        return complexs.size
    }

    class ComplexHolder(val view: View): RecyclerView.ViewHolder(view){

        val textViewComplexName: TextView = view.findViewById(R.id.tvComplexName)
        val textViewComplexPhone: TextView = view.findViewById(R.id.tvComplexPhone)

        fun render(complex: Complex){
            textViewComplexName.text = complex.name
            textViewComplexPhone.text = complex.phone

            view.setOnClickListener {
                Toast.makeText(view.context, "Has seleccionado a ${complex.name}", Toast.LENGTH_SHORT).show()

                val detailComplexIntent = Intent(view.context, DetailComplex::class.java)
                detailComplexIntent.putExtra("objectComplex", complex)
                startActivity( view.context,detailComplexIntent, null)

            }
        }
    }

}