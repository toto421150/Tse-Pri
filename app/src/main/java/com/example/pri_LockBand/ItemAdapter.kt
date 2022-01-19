package com.example.pri_LockBand

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.itemantennadata.view.*

class ItemAdapter( //Permet d'afficher la liste d'antennes dans la liste view
    private val AntennaItems: MutableList<AntennaItem>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.itemantennadata,
                    parent,
                    false
                )
            )
    }

    fun addNewVals(AntennaItem: AntennaItem) {
        AntennaItems.add(AntennaItem)

        notifyItemInserted(AntennaItems.size - 1)
    }

    fun deleteOldVals() {
        AntennaItems.removeAll(AntennaItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val currentItem = AntennaItems[position]
        holder.itemView.apply {
            antennaData.text = currentItem.data
            antennaId.text= "Antenna n°"+ position.toString()
            if(position==0){
                antennaId.text = "Connected Antenna"
                antennaData.setTypeface(Typeface.DEFAULT_BOLD)
            }

        }
    }

    override fun getItemCount(): Int {
        return AntennaItems.size
    }
}


















