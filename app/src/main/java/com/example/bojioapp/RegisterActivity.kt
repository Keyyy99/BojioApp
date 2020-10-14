package com.example.bojioapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import com.example.bojioapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        signUpbtn.setOnClickListener{
            signUpUser()
        }


        val signInbtn = findViewById(R.id.signIn_text) as TextView

        signInbtn.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    fun signUpUser(){

        if(reusername.text.toString().isEmpty()){
            reusername.error = "Please enter Username"
            reusername.requestFocus()
            return
        }

        if(reemailtxt.text.toString().isEmpty()){
            reemailtxt.error = "Please enter Email"
            reemailtxt.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(reemailtxt.text.toString()).matches()){
            reemailtxt.error = "Please enter correct Email format"
            reemailtxt.requestFocus()
            return
        }

        if(lopasstxt.text.toString().isEmpty()) {
            lopasstxt.error = "Please enter Password"
            lopasstxt.requestFocus()
            return
        }

        if(confirmpasstxt.text.toString().isEmpty()){
            confirmpasstxt.error = "Please enter Confirm Password"
            confirmpasstxt.requestFocus()
            return
        }

        if (lopasstxt.text.toString() != confirmpasstxt.text.toString()){
            confirmpasstxt.error = "Confirm Password is Different"
            confirmpasstxt.requestFocus()
            return
        }

        val email = reemailtxt.text.toString()
        val pass = lopasstxt.text.toString()
        val username = reusername.text.toString()
        val age = "-"
        val gender = "-"
        val phone = "-"
        val photo = "https://firebasestorage.googleapis.com/v0/b/bojioapp-bf834.appspot.com/o/nopic%2Fprofile.png?alt=media&token=ee7901b5-e12e-411b-9d5a-b186f32c3e5e"
        val intro = "-"

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Sign in success
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            val userId = auth!!.currentUser!!.uid
                        val user = mkUser(username,email)
                var reference = mDatabase.child("users").child(userId)
                reference.setValue(user)
                    .addOnCompleteListener{
                        if (task.isSuccessful){
                            val uid = FirebaseAuth.getInstance().uid

                            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

                            val user = User(username,age,gender,phone,email,intro,photo,userId)

                            ref.setValue(user)
                            Toast.makeText(baseContext, "Register successfully",
                                Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(baseContext, "Something went wrong, please try again later",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                        }

                } else {
                    // If sign in fails
                    Toast.makeText(baseContext, "Email already exist",
                        Toast.LENGTH_SHORT).show()
                }

            }

    }

    private fun mkUser(username:String, email: String): User {
        return User(name = username,email = email)
    }
}
