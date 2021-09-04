package com.poochaser.poochaser.ui.clock

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.poochaser.poochaser.R
import kotlinx.android.synthetic.main.item.view.*
import java.time.Clock

class CalendarAdapter(private val context: Context) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private lateinit var itemClickListener: OnitemClickListener
    var datas = mutableListOf<Data>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    interface OnitemClickListener {
        fun onClick(v: View, position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val clock: TextView = itemView.findViewById(R.id.itemClock)
        private val type: ImageView = itemView.findViewById(R.id.itemTypeImage)
        private val color: ImageView = itemView.findViewById(R.id.itemColorImage)

        fun bind(item: Data){
            clock.text = item.clock
            if (item.type == 1)
                type.setImageResource(R.drawable.type1)
            else if (item.type == 2)
                type.setImageResource(R.drawable.type2)
            else if (item.type == 3)
                type.setImageResource(R.drawable.type3)
            else if (item.type == 4)
                type.setImageResource(R.drawable.type4)
            else if (item.type == 5)
                type.setImageResource(R.drawable.type5)
            else if (item.type == 6)
                type.setImageResource(R.drawable.type6)
            else if (item.type == 7)
                type.setImageResource(R.drawable.type7)
            if (item.color == "red")
                color.setImageResource(R.drawable.red)
            else if (item.color == "orange")
                color.setImageResource(R.drawable.orange)
            else if (item.color == "brown")
                color.setImageResource(R.drawable.brown)
            else if (item.color == "gray")
                color.setImageResource(R.drawable.gray)
            else if (item.color == "black")
                color.setImageResource(R.drawable.black)
            else if (item.color == "yellow")
                color.setImageResource(R.drawable.yellow)
        }
    }
}