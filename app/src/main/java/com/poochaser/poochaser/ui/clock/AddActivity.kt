package com.poochaser.poochaser.ui.clock

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.poochaser.poochaser.R
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    var clock: String? = ""
    var type: Int = 0
    var color: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        backButton.setOnClickListener {
            finish()
        }
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        timeTextView2.text = timeFormat.format(System.currentTimeMillis())
        timeTextView2.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ timepicker, h, m ->
                var state = "AM"
                var hour = h
                if(h>12) {
                    state = "PM"
                    hour -= 12
                }
                timeTextView2.text = String.format("%02d:%02d "+state, hour, m)
            },cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }
        informationImageView.setOnClickListener {
            val informationDialog = Dialog(this, R.style.TransparentDialog)
            val informationDialogView = layoutInflater.inflate(R.layout.information_dialog, null)
            informationDialog.setContentView(informationDialogView)
            val informationDialogBackButton = informationDialog.findViewById<ImageButton>(R.id.informationDialogBackButton)
            informationDialogBackButton.setOnClickListener {
                informationDialog.dismiss()
            }
            informationDialog.show()
        }
        type1.setOnClickListener {
            type = 1
            typeReset()
            type1ImageView.setImageResource(R.drawable.type1_selected)
        }
        type2.setOnClickListener {
            type = 2
            typeReset()
            type2ImageView.setImageResource(R.drawable.type2_selected)
        }
        type3.setOnClickListener {
            type = 3
            typeReset()
            type3ImageView.setImageResource(R.drawable.type3_selected)
        }
        type4.setOnClickListener {
            type = 4
            typeReset()
            type4ImageView.setImageResource(R.drawable.type4_selected)
        }
        type5.setOnClickListener {
            type = 5
            typeReset()
            type5ImageView.setImageResource(R.drawable.type5_selected)
        }
        type6.setOnClickListener {
            type = 6
            typeReset()
            type6ImageView.setImageResource(R.drawable.type6_selected)
        }
        type7.setOnClickListener {
            type = 7
            typeReset()
            type7ImageView.setImageResource(R.drawable.type7_selected)
        }
        redColor.setOnClickListener {
            color = "red"
            colorReset()
            redImageView.setImageResource(R.drawable.red_selected)
        }
        orangeColor.setOnClickListener {
            color = "orange"
            colorReset()
            orangeImageView.setImageResource(R.drawable.orange_selected)
        }
        brownColor.setOnClickListener {
            color = "brown"
            colorReset()
            brownImageView.setImageResource(R.drawable.brown_selected)
        }
        grayColor.setOnClickListener {
            color = "gray"
            colorReset()
            grayImageView.setImageResource(R.drawable.gray_selected)
        }
        blackColor.setOnClickListener {
            color = "black"
            colorReset()
            blackImageView.setImageResource(R.drawable.black_selected)
        }
        yellowColor.setOnClickListener {
            color = "yellow"
            colorReset()
            yellowImageView.setImageResource(R.drawable.yellow_selected)
        }
        addButton.setOnClickListener {
            clock = timeTextView2.text.toString()
            if(type == 0 || color == ""){
                val dialog = Dialog(this, R.style.TransparentDialog)
                val dialogView = layoutInflater.inflate(R.layout.add_dialog, null)
                dialog.setContentView(dialogView)
                val addDialogButton = dialog.findViewById<Button>(R.id.addDialogButton)
                addDialogButton.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
            else{
                intent.putExtra("clock", clock)
                intent.putExtra("type", type)
                intent.putExtra("color", color)
                setResult(1, intent)
                if (intent.getIntExtra("requestcode", 1) == 2)
                    setResult(2, intent)
                finish()
            }
        }
        if (intent.getIntExtra("requestcode", 1) == 2)
            edit()
    }

    private fun typeReset(){
        type1ImageView.setImageResource(R.drawable.type1)
        type2ImageView.setImageResource(R.drawable.type2)
        type3ImageView.setImageResource(R.drawable.type3)
        type4ImageView.setImageResource(R.drawable.type4)
        type5ImageView.setImageResource(R.drawable.type5)
        type6ImageView.setImageResource(R.drawable.type6)
        type7ImageView.setImageResource(R.drawable.type7)
    }

    private fun colorReset(){
        redImageView.setImageResource(R.drawable.red)
        orangeImageView.setImageResource(R.drawable.orange)
        brownImageView.setImageResource(R.drawable.brown)
        grayImageView.setImageResource(R.drawable.gray)
        blackImageView.setImageResource(R.drawable.black)
        yellowImageView.setImageResource(R.drawable.yellow)
    }

    private fun edit(){
        clock = intent.getStringExtra("clock")
        type = intent.getIntExtra("type", 0)
        color = intent.getStringExtra("color")
        if(type == 1)
            type1ImageView.setImageResource(R.drawable.type1_selected)
        else if(type == 2)
            type2ImageView.setImageResource(R.drawable.type2_selected)
        else if(type == 3)
            type3ImageView.setImageResource(R.drawable.type3_selected)
        else if(type == 4)
            type4ImageView.setImageResource(R.drawable.type4_selected)
        else if(type == 5)
            type5ImageView.setImageResource(R.drawable.type5_selected)
        else if(type == 6)
            type6ImageView.setImageResource(R.drawable.type6_selected)
        else if(type == 7)
            type7ImageView.setImageResource(R.drawable.type7_selected)
        if(color == "red")
            redImageView.setImageResource(R.drawable.red_selected)
        else if(color == "orange")
            orangeImageView.setImageResource(R.drawable.orange_selected)
        else if(color == "brown")
            brownImageView.setImageResource(R.drawable.brown_selected)
        else if(color == "gray")
            grayImageView.setImageResource(R.drawable.gray_selected)
        else if(color == "black")
            blackImageView.setImageResource(R.drawable.black_selected)
        else if(color == "yellow")
            yellowImageView.setImageResource(R.drawable.yellow_selected)
    }
}