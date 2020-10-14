package com.example.bojioapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.bojioapp.model.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.IOException
import java.lang.Double.parseDouble
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null

    private var Name: EditText? = null
    private var Age: EditText? = null
    private var Gender: EditText? = null
    private var Phone: EditText? = null
    private var Email: EditText? = null
    private var Intro: EditText? = null
    lateinit var photo:String

    //picture
    private var viewImg: ImageView? = null
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        super.onPause()
        overridePendingTransition(0, 0)
        supportActionBar?.title = "Edit Profile"

        viewImg = findViewById<ImageView>(R.id.profImg2) as ImageView

        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        initialise()

        imgbtn.setOnClickListener{
            ImagePicker()
        }


        updatebtn.setOnClickListener{
            val name = findViewById<EditText>(R.id.profUsernametxt) as EditText
            val age = findViewById<EditText>(R.id.profAgetxt) as EditText
            val email = findViewById<EditText>(R.id.profEmailtxt) as EditText
            val gender = findViewById<EditText>(R.id.profGentxt) as EditText
            val intro = findViewById<EditText>(R.id.profIntrotxt) as EditText
            val phone = findViewById<EditText>(R.id.profPhonetxt) as EditText

            var numeric = true

            var errorCount = 0

            if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
                email.error = "Please enter correct Email format"
                email.requestFocus()
                errorCount++
                return@setOnClickListener
            }else{
                errorCount = 0
            }


            try {
                val num = parseDouble(age.text.toString())
            } catch (e: NumberFormatException) {
                numeric = false

                if (!numeric) {
                    age.error = "Age does not contain number"
                    age.requestFocus()
                    errorCount++
                    return@setOnClickListener
                } else {
                    errorCount = 0
                }
            }

            try {
                val num = parseDouble(phone.text.toString())
            } catch (e: NumberFormatException) {
                numeric = false
                errorCount++

                if (!numeric) {
                    phone.error = "Phone number can only contain number"
                    phone.requestFocus()
                    errorCount++
                    return@setOnClickListener
                } else {
                    errorCount = 0
                }

            }

            if (errorCount == 0){
                try {

                    val ref = FirebaseDatabase.getInstance().getReference("users")

                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(data: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val requestId = mAuth!!.currentUser!!.uid

                            val updateProf = User(name.text.toString(),
                                age.text.toString(),
                                gender.text.toString(),
                                phone.text.toString(),
                                email.text.toString(),
                                intro.text.toString(),
                                photo,
                                requestId.toString())
                            ref.child(requestId).setValue(updateProf)

                            Toast.makeText(baseContext, "Update Successful",
                                Toast.LENGTH_SHORT).show()

                            onBackPressed()

                        }
                    })
                }catch(ex: Exception){
                }


            }
            else{
                Toast.makeText(baseContext, "Unable to update profile",
                    Toast.LENGTH_SHORT).show()
            }
        }

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Edit Profile"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    //picture
    private fun ImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
                Picasso.get().load(filePath).into(viewImg)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            ref?.putFile(filePath!!)?.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                ref.downloadUrl.addOnSuccessListener {
                    photo = it.toString()
                }
            })
        }else{
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initialise() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("users")
        mAuth = FirebaseAuth.getInstance()

        Name = findViewById<EditText>(R.id.profUsernametxt) as EditText
        Age = findViewById<EditText>(R.id.profAgetxt) as EditText
        Gender = findViewById<EditText>(R.id.profGentxt) as EditText
        Phone = findViewById<EditText>(R.id.profPhonetxt) as EditText
        Email = findViewById<EditText>(R.id.profEmailtxt) as EditText
        Intro = findViewById<EditText>(R.id.profIntrotxt) as EditText
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        Email?.setText(mUser.email)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Name?.setText(snapshot.child("name").value as? String)
                Age?.setText(snapshot.child("age").value as? String)
                Gender?.setText(snapshot.child("gender").value as? String)
                Phone?.setText(snapshot.child("phone").value as? String)
                Intro?.setText(snapshot.child("intro").value as? String)

                photo = (snapshot.child("photo").value as? String).toString()

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val fragment=ProfileFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.edit_pro,fragment)
        fragmentTransaction.commit()
        finish()
    }

}
