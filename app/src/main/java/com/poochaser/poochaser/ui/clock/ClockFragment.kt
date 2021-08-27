package com.poochaser.poochaser.ui.clock

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.poochaser.poochaser.R
import kotlinx.android.synthetic.main.fragment_clock.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class ClockFragment : Fragment() {

    private lateinit var clockViewModel: ClockViewModel
    lateinit var dataAdapter: DataAdapter
    val datas = mutableListOf<Data>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clockViewModel =
            ViewModelProviders.of(this).get(ClockViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_clock, container, false)
        val addButton: ImageButton = root.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val addIntent = Intent(root.context, AddActivity::class.java)
            startActivityForResult(addIntent, 1)
        }
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataAdapter = DataAdapter(requireContext())
        recyclerView.adapter = dataAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 2){
            datas.apply{
                val clock = data!!.getStringExtra("clock")
                val type = data.getIntExtra("type", 1)
                val color = data.getStringExtra("color")
                add(Data(clock, type, color))

                dataAdapter.datas = datas
                dataAdapter.notifyDataSetChanged()
            }
        }
    }
}