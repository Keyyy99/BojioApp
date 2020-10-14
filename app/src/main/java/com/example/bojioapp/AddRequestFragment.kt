package com.example.bojioapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_addrequest.*
import java.lang.Exception
import java.util.*

class AddRequestFragment : Fragment() {

    private  lateinit var dpd:DatePickerDialog
    private  lateinit var dpdWord:String
    private  lateinit var tdp:String

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var nameView: TextView
    private lateinit var Email: String
    private lateinit var host: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (context as AppCompatActivity).supportActionBar!!.title = "Add Request"
        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_addrequest,container,false)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("users")
        mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        Email=mUser.email as String

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txtUid?.setText(snapshot.child("uid").value as? String)

                mDatabase = FirebaseDatabase.getInstance()
                mDatabaseReference = mDatabase!!.reference!!.child("users")
                mAuth = FirebaseAuth.getInstance()

                nameView = root.findViewById(R.id.txtUid)

                val mUser = mAuth!!.currentUser
                host = mUser!!.uid
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val c: Calendar = Calendar.getInstance()
        val currentDay =  c.get(Calendar.DAY_OF_MONTH)
        val currentMonth = c.get(Calendar.MONTH)
        val currentYear = c.get(Calendar.YEAR)

        dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener{ view, year, month, day -> date.setText(day.toString() +
                "/" + (month+1).toString() + "/" + year.toString()) }
            , currentYear, currentMonth, currentDay)

        dpdWord = "$currentDay/"+(currentMonth+1)+"/$currentYear"

        txtDate.setOnClickListener(){
            dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener{ view, year, month, day -> date.setText(day.toString() +
                    "/" + (month+1).toString() + "/" + year.toString() )

                dpdWord = day.toString()+ "/" + (month+1).toString() + "/" + year.toString()
            }
                , currentYear, currentMonth, currentDay)
            dpd.datePicker.setMinDate(System.currentTimeMillis()-1000)
            dpd.show()
        }

        var hour = c.get(Calendar.HOUR)
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

        tdp=c.get(Calendar.HOUR).toString()+":"+c.get(Calendar.MINUTE).toString()+am_pm

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
            if (timeView != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val msg = "Time is: $hour:$min $am_pm"
                tdp = "$hour:$min $am_pm"
                timeView.text = msg
                timeView.visibility = ViewGroup.VISIBLE
            }
        }

        btnSave.setOnClickListener(){
            addRequest()
        }
    }

    fun addRequest(){

        try{
        if(title.text.toString().trim().isEmpty()){
            title.error = "Please fill in the Title Name"
            title.requestFocus()
            return
        }


        if(location.text.toString().trim().isEmpty()){
            location.error = "Please fill in the Location"
            location.requestFocus()
            return
        }

        val title = title.text.toString().trim()
        val category: String = category.getSelectedItem().toString()
        val participantNum= participantNum.getSelectedItem().toString()
        val date =dpdWord
        val time = tdp
            val uid=host
        val location = location.text.toString().trim()
        val description = desc.text.toString()

            try {

                val ref = FirebaseDatabase.getInstance().getReference("requests")

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val requestId = "R" + (snapshot.childrenCount + 1).toString()

                        val requestDetail = RequestDetail(
                            requestId,
                            title,
                            category,
                            date,
                            uid,
                            location,
                            participantNum,
                            time,
                            description
                        )
                        ref.child(requestId).setValue(requestDetail).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Request added successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Request fail to add", Toast.LENGTH_SHORT).show()
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.newChat -> {
                val intent = Intent(context, NewChat::class.java)
                startActivity(intent)
                true
            }R.id.signOut->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(context,LoginActivity::class.java))
                true
            }else -> {

                return super.onOptionsItemSelected(item)
            }

        }
    }
}