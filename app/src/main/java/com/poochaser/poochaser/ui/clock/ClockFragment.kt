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
    val user = Firebase.auth.currentUser
    val db = FirebaseFirestore.getInstance()
    var year : String = ""
    var month : String = ""
    var day : String = ""

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
            addIntent.putExtra("requestcode", 1)
            startActivityForResult(addIntent, 1)
        }
        val timeFormat = SimpleDateFormat("yyyy")
        year = timeFormat.format(System.currentTimeMillis())
        timeFormat.applyPattern("MM")
        month = timeFormat.format(System.currentTimeMillis())
        timeFormat.applyPattern("dd")
        day = timeFormat.format(System.currentTimeMillis())

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
                    val editIntent = Intent(requireContext(), AddActivity::class.java)
                    editIntent.putExtra("clock", dataAdapter.datas[position].clock)
                    editIntent.putExtra("type", dataAdapter.datas[position].type)
                    editIntent.putExtra("color", dataAdapter.datas[position].color)
                    editIntent.putExtra("requestcode", 2)
                    editIntent.putExtra("position", position)
                    startActivityForResult(editIntent, 2)
                    dialog.dismiss()
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
                        db.collection(user!!.uid).document(year).collection(month).document(day)
                            .collection("data").document(time12to24(dataAdapter.datas[position].clock)).delete()
                        dialog.dismiss()
                        dataTransfer()
                    }
                    deleteDialog.show()
                }
                cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
            }
        })
        dataTransfer()
    }

    fun time12to24(time : String): String {
        if(time[6] == 'A')
            return time.substring(0,5)
        else
            return "" + (time.substring(0,2).toInt() + 12) + time.substring(2,5)
    }

    fun time24to12(time: String): String{
        if(time.substring(0,2).toInt() > 12)
            return "" + (time.substring(0,2).toInt() - 12) + time.substring(2,5) + " PM"
        else
            return time + " AM"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 1){
            val clock = data!!.getStringExtra("clock")
            val type = data.getIntExtra("type", 1)
            val color = data.getStringExtra("color")
            Log.d("clock",clock)
            var newClock = time12to24(clock)
            db.collection(user!!.uid).document(year).collection(month).document(day)
                .collection("data").document(newClock).set(hashMapOf("type" to type, "color" to color))
            dataTransfer()
        }
        if(resultCode == 2){
            val position = data!!.getIntExtra("position", 1)
            val beforeClock = time12to24(dataAdapter.datas[position].clock)
            val afterClock = time12to24(data!!.getStringExtra("clock"))
            val type = data.getIntExtra("type", 1)
            val color = data.getStringExtra("color")
            db.collection(user!!.uid).document(year).collection(month).document(day)
                .collection("data").document(beforeClock).delete()
            db.collection(user!!.uid).document(year).collection(month).document(day)
                .collection("data").document(afterClock).set(hashMapOf("type" to type, "color" to color))
            dataTransfer()
        }
    }

    fun dataTransfer(){
        dataAdapter.datas.clear()
        db.collection(user!!.uid).document(year).collection(month).document(day)
            .collection("data").get().addOnSuccessListener { documents ->
                for(document in documents){
                    dataAdapter.datas.add(Data(time24to12(document.id),
                        document.get("type").toString().toInt(), document.getString("color")!!))
                }
                dataAdapter.notifyDataSetChanged()
            }
    }
}