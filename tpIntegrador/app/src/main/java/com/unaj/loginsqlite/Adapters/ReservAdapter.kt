package com.unaj.loginsqlite.Adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unaj.loginsqlite.R
import com.unaj.loginsqlite.model.Reservation


class ReservAdapter(val reservs:List<Reservation>,
                    private var optionsMenuClickListener: OptionsMenuClickListener):
    RecyclerView.Adapter<ReservAdapter.ReservHolder>(){

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }


    private fun getoptionsMenuClickListener(): OptionsMenuClickListener{
        return this.optionsMenuClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ReservHolder(layoutInflater.inflate(R.layout.item_reserv, parent, false))
    }

    override fun onBindViewHolder(holder: ReservHolder, position: Int) {
        //holder.render(reservs[position])
        with(holder){
            with(reservs[position]){
                val textViewComplexName: TextView = view.findViewById(R.id.tvComplexName)
                textViewComplexName.text = this.complexName
                val textViewReservDate: TextView = view.findViewById(R.id.tvDate)
                textViewReservDate.text = this.date
                val textViewReservTime: TextView = view.findViewById(R.id.tvTime)
                textViewReservTime.text = this.time
                val textViewOptions: TextView = view.findViewById(R.id.textViewOptions)
                textViewOptions.setOnClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(position)
                }
            }

        }

    }

    override fun getItemCount(): Int = reservs.size


    class ReservHolder(val view: View):RecyclerView.ViewHolder(view){
        val textViewComplexName: TextView = view.findViewById(R.id.tvComplexName)
        val textViewReservDate: TextView = view.findViewById(R.id.tvDate)
        val textViewReservTime: TextView = view.findViewById(R.id.tvTime)
        val textViewOptions: TextView = view.findViewById(R.id.textViewOptions)

        fun render(reserv: Reservation){
            textViewComplexName.text = reserv.complexName
            textViewReservDate.text = reserv.date
            textViewReservTime.text = reserv.time

        }
    }


}