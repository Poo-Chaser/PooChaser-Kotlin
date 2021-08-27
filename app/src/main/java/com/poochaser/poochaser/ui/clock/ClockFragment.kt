package com.poochaser.poochaser.ui.clock

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.poochaser.poochaser.R
import kotlinx.android.synthetic.main.fragment_clock.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class ClockFragment : Fragment() {

    private lateinit var clockViewModel: ClockViewModel
    lateinit var dataAdapter: DataAdapter
    val datas = mutableListOf<Data>()
    var editContext: Context? = null

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
            editContext = root.context
            addIntent.putExtra("requestcode", 1)
            startActivityForResult(addIntent, 1)
        }
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataAdapter = DataAdapter(requireContext())
        recyclerView.adapter = dataAdapter
        dataAdapter.setItemClickListener(object: DataAdapter.OnitemClickListener{
            override fun onClick(v: View, position: Int) {
                val dialogView = layoutInflater.inflate(R.layout.modal_bottom_sheet, null)
                val editButton = dialogView.findViewById<TextView>(R.id.editButton)
                val deleteButton = dialogView.findViewById<TextView>(R.id.deleteButton)
                val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
                val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
                dialog.setContentView(dialogView)
                dialog.show()
                editButton.setOnClickListener {
                    edit(position)
                }
                deleteButton.setOnClickListener {
                    datas.removeAt(position)
                    dialog.dismiss()
                    dataAdapter.notifyDataSetChanged()
                }
                cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 1){
            datas.apply{
                val clock = data!!.getStringExtra("clock")
                val type = data.getIntExtra("type", 1)
                val color = data.getStringExtra("color")
                add(Data(clock, type, color))

                dataAdapter.datas = datas
                dataAdapter.notifyDataSetChanged()
            }
        }
        if(resultCode == 2){
            val position = data!!.getIntExtra("position", 1)
            dataAdapter.datas[position].clock = data!!.getStringExtra("clock")
            dataAdapter.datas[position].type = data.getIntExtra("type", 1)
            dataAdapter.datas[position].color = data.getStringExtra("color")
            dataAdapter.notifyDataSetChanged()
        }
    }

    fun edit(position: Int){
        val editIntent = Intent(editContext, AddActivity::class.java)
        editIntent.putExtra("requestcode", 2)
        editIntent.putExtra("position", position)
        startActivityForResult(editIntent, 2)
    }
}