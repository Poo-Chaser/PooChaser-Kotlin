package com.poochaser.poochaser.ui.clock

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.poochaser.poochaser.R
import kotlinx.android.synthetic.main.fragment_clock.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class ClockFragment : Fragment() {

    private lateinit var clockViewModel: ClockViewModel
    lateinit var dataAdapter: DataAdapter
    val datas = mutableListOf<Data>()
    var editContext: Context? = null
    val user = Firebase.auth.currentUser
    val db = FirebaseFirestore.getInstance()

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
        val timeFormat = SimpleDateFormat("MM/dd hh:mm a", Locale.ENGLISH)
        timeTextClock.format12Hour = timeFormat.toLocalizedPattern()
        timeTextClock.textLocale = Locale.ENGLISH
        timeTextClock.refreshDrawableState()
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
                    val editIntent = Intent(editContext, AddActivity::class.java)
                    editIntent.putExtra("clock", dataAdapter.datas[position].clock)
                    editIntent.putExtra("type", dataAdapter.datas[position].type)
                    editIntent.putExtra("color", dataAdapter.datas[position].color)
                    editIntent.putExtra("requestcode", 2)
                    editIntent.putExtra("position", position)
                    startActivityForResult(editIntent, 2)
                }
                deleteButton.setOnClickListener {
                    val deleteDialogView = layoutInflater.inflate(R.layout.delete_dialog, null)
                    val deleteDialog = Dialog(requireContext(),R.style.TransparentDialog)
                    deleteDialog.setContentView(deleteDialogView)
                    val deleteDialogCancelButton = deleteDialog.findViewById<Button>(R.id.deleteDialogCancelButton)
                    val deleteDialogDeleteButton = deleteDialog.findViewById<Button>(R.id.deleteDialogDeleteButton)
                    deleteDialogCancelButton.setOnClickListener {
                        deleteDialog.dismiss()
                        dialog.dismiss()
                    }
                    deleteDialogDeleteButton.setOnClickListener {
                        deleteDialog.dismiss()
                        datas.removeAt(position)
                        dialog.dismiss()
                        dataAdapter.notifyDataSetChanged()
                    }
                    deleteDialog.show()
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
                Log.d("clock",clock)
                val timeFormat = SimpleDateFormat("yyyy")
                val year : String = timeFormat.format(System.currentTimeMillis())
                timeFormat.applyPattern("MM")
                val month : String = timeFormat.format(System.currentTimeMillis())
                timeFormat.applyPattern("dd")
                val day : String = timeFormat.format(System.currentTimeMillis())
                var newClock : String = ""
                if (clock[6]=='A')
                    newClock = clock.substring(0,5)
                else{
                    val newHour = 12 + clock.substring(0,2).toInt()
                    newClock = "" + newHour + clock.substring(2,5)
                }
                db.collection(user!!.uid).document(year).collection(month).document(day)
                    .collection("data").document(newClock).set(hashMapOf("type" to type, "color" to color))

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
}