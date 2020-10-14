package com.example.bojioapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bojioapp.model.Notification
import com.example.bojioapp.model.getUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_viewrequest.*
import java.lang.Exception
import java.lang.ref.Reference

class ViewRequest: AppCompatActivity() {
    companion object{
        val USER_KEY = "USER_KEY"
        var current: getUser? =null

    }

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var nameView: TextView
    private lateinit var Email: String

    private fun initialise() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("users")
        mAuth = FirebaseAuth.getInstance()

        nameView = findViewById(R.id.txtName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_viewrequest)
        super.onPause()
        overridePendingTransition(0, 0)

        getCurrentUser()

        supportActionBar?.title = "Request Detail"

        val bundle:Bundle?=intent.extras
        val Rid=bundle!!.getString("Firebase_Rid")
        val Host=bundle!!.getString("Firebase_Host")
        val Title=bundle!!.getString("Firebase_Title")
        val Date=bundle!!.getString("Firebase_Date")
        val Time=bundle!!.getString("Firebase_Time")
        val Location=bundle!!.getString("Firebase_Location")
        val Category=bundle!!.getString("Firebase_Category")
        val Description=bundle!!.getString("Firebase_Description")
        val Participant=bundle!!.getString("Firebase_Participant")

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("users")
        mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        Email=mUser.email as String

        val btnE = findViewById<Button>(R.id.btnEdit)
        val btnJ = findViewById<Button>(R.id.btnJoin)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txtName?.setText(snapshot.child("uid").value as? String)

                initialise()

                if (txtName.text.trim()==Host) {
                    btnE.visibility = View.VISIBLE
                    btnJ.visibility = View.GONE
                } else{
                    btnJ.visibility = View.VISIBLE
                    btnE.visibility = View.GONE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        title_View.setText(Title)
        date_View.setText(Date)
        time_View.setText(Time)
        location_View.setText(Location)
        category_View.setText(Category)
        desc_View.setText(Description)
        pNum_View.setText(Participant)

        btnE.setOnClickListener(){

            bundle.putString("Rid", Rid)
            bundle.putString("Title", Title)
            bundle.putString("Host", Host)
            bundle.putString("Date", Date)
            bundle.putString("Time", Time)
            bundle.putString("Location", Location)
            bundle.putString("Category", Category)
            bundle.putString("Description", Description)
            bundle.putString("Participant", Participant)

            startFrag(EditRequestFragment(),bundle)
        }

        btnJ.setOnClickListener() {
            val bundle:Bundle?=intent.extras
            val Rid=bundle!!.getString("Firebase_Rid")
            val Host=bundle!!.getString("Firebase_Host")
            val Title=bundle!!.getString("Firebase_Title")
            val  user = FirebaseAuth.getInstance().uid

            val text =  current?.name + " has joined your event - " + Title
            val intent = Intent(this, MainActivity::class.java)

            val ref = FirebaseDatabase.getInstance().getReference("/notification/$Host").push()
            val noti = Notification(user!!, System.currentTimeMillis()/1000, text)
            ref.setValue(noti)

            startActivity(intent)
            Toast.makeText(this, "Successfully Join Event", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun getCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                current = p0.getValue(getUser::class.java)

                Log.d("NewMessages","Current User ${current?.name}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun startFrag(fragment: Fragment,bundle: Bundle){
        fragment.setArguments(bundle)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.viewLayout,fragment)
        fragmentTransaction.commit()
    }
}