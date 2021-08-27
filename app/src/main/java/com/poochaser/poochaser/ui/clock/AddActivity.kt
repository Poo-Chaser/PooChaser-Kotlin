package com.poochaser.poochaser.ui.clock

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.poochaser.poochaser.R
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var clock: String? = ""
        var type: Int = 1
        var color: String? = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        backButton.setOnClickListener {
            finish()
        }
        val timeFormat = SimpleDateFormat("hh:mm a")
        timeTextView2.text = timeFormat.format(System.currentTimeMillis())
        timeTextView2.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ timepicker, h, m ->
                var state = "오전"
                var hour = h
                if(h>12) {
                    state = "오후"
                    hour -= 12
                }
                timeTextView2.text = String.format("%02d:%02d "+state, hour, m)
            },cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }
        type1.setOnClickListener {
            type = 1
        }
        type2.setOnClickListener {
            type = 2
        }
        type3.setOnClickListener {
            type = 3
        }
        type4.setOnClickListener {
            type = 4
        }
        type5.setOnClickListener {
            type = 5
        }
        type6.setOnClickListener {
            type = 6
        }
        type7.setOnClickListener {
            type = 7
        }
        redColor.setOnClickListener {
            color = "red"
        }
        orangeColor.setOnClickListener {
            color = "orange"
        }
        brownColor.setOnClickListener {
            color = "brown"
        }
        grayColor.setOnClickListener {
            color = "gray"
        }
        blackColor.setOnClickListener {
            color = "black"
        }
        yellowColor.setOnClickListener {
            color = "yellow"
        }
        addButton.setOnClickListener {
            val intent = Intent()
            clock = timeTextView2.text.toString()
            intent.putExtra("clock", clock)
            intent.putExtra("type", type)
            intent.putExtra("color", color)
            setResult(2, intent)
            finish()
        }
    }
}