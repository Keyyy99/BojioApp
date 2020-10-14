package com.example.bojioapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_editrequest.*
import java.lang.Exception
import java.util.*

private lateinit var Rid:String

class EditRequestFragment: Fragment() {

    private  lateinit var dpd:DatePickerDialog
    private  lateinit var dpdWord:String
    private  lateinit var tdp:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (context as AppCompatActivity).supportActionBar!!.title = "Edit Request"
        return inflater.inflate(R.layout.fragment_editrequest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val c: Calendar = Calendar.getInstance()
        val currentDay =  c.get(Calendar.DAY_OF_MONTH)
        val currentMonth = c.get(Calendar.MONTH)
        val currentYear = c.get(Calendar.YEAR)

        Rid=arguments!!.getString("Rid").toString()
        val Title=arguments!!.getString("Title")
        val Host=arguments!!.getString("Host")
        val Date=arguments!!.getString("Date")
        val Time=arguments!!.getString("Time")
        val Location=arguments!!.getString("Location")
        val Category=arguments!!.getString("Category")
        val Description=arguments!!.getString("Description")
        val Participant=arguments!!.getString("Participant")

        title2.setText(Title)
        date2.setText(Date)
        timeView2.setText(Time)
        location2.setText(Location)
        category2.setSelection(getIndex(category2, Category.toString()))
        desc2.setText(Description)
        participantNum2.setSelection(getIndex(participantNum2, Participant.toString()))

        dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener{ view, year, month, day -> date2.setText(day.toString() +
                "/" + (month+1).toString() + "/" + year.toString()) }
            , currentYear, currentMonth, currentDay)

        dpdWord =Date.toString()

        txtDate.setOnClickListener(){
            dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener{ view, year, month, day -> date2.setText(day.toString() +
                    "/" + (month+1).toString() + "/" + year.toString() )

                dpdWord = day.toString()+ "/" + (month+1).toString() + "/" + year.toString()
            }
                , currentYear, currentMonth, currentDay)
            dpd.datePicker.setMinDate(System.currentTimeMillis()-1000)
            dpd.show()
        }

        tdp=Time.toString()

        timePicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
            var am_pm = ""
            // AM_PM decider logic
            when {hour == 0 -> { hour += 12
                am_pm = "AM"
            }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> { hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            if (timeView2 != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val msg = "Time is: $hour:$min $am_pm"
                tdp = "$hour:$min $am_pm"
                timeView2.text = msg
                timeView2.visibility = ViewGroup.VISIBLE
            }
        }

        btnUpdate.setOnClickListener(){
            editRequest(Host.toString())
        }
    }

    fun editRequest(ownerR:String){

        try{
            if(title2.text.toString().trim().isEmpty()){
                title2.error = "Please fill in the Title Name"
                title2.requestFocus()
                return
            }


            if(location2.text.toString().trim().isEmpty()){
                location2.error = "Please fill in the Location"
                location2.requestFocus()
                return
            }

            val title = title2.text.toString().trim()
            val category: String = category2.getSelectedItem().toString()
            val participantNum= participantNum2.getSelectedItem().toString()
            val date =dpdWord
            val time = tdp
            val host = ownerR
            val location = location2.text.toString().trim()
            val description = desc2.text.toString()

            try {

                val ref = FirebaseDatabase.getInstance().getReference("requests")

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val requestId = Rid

                        val requestDetail = RequestDetail(
                            requestId,
                            title,
                            category,
                            date,
                            host,
                            location,
                            participantNum,
                            time,
                            description
                        )
                        ref.child(requestId).setValue(requestDetail).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Request update successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Request fail to update", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                })
            }catch(ex: Exception){
                Toast.makeText(context,ex.message, Toast.LENGTH_LONG).show()
            }
        }catch(ex: Exception){
            Toast.makeText(context,ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }

        return 0
    }

}