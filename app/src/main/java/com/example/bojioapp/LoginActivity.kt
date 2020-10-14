package com.example.bojioapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.lopasstxt

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val signUpbtn = findViewById(R.id.signUp_text) as TextView

        signUpbtn.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        signInbtn.setOnClickListener{
            doLogin()
        }

        forgotpasstxt.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Email Address")
            val view = layoutInflater.inflate(R.layout.forgot_password,null)
            val forgotemail: EditText = view.findViewById<EditText>(R.id.forgotemailtxt)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _->
                forgotPassword(forgotemail)
            })
            val show2= builder.show()
            builder.setNegativeButton("close", DialogInterface.OnClickListener { _, _ ->
                if(show2 != null && show2.isShowing()) {
                    show2.dismiss()
                }
            })

        }
    }

    private fun forgotPassword(forgotemail : EditText){
        val user = FirebaseAuth.getInstance().currentUser

        if(!Patterns.EMAIL_ADDRESS.matcher(forgotemail.text.toString()).matches()){
            forgotemail.error = "Please enter correct Email format"
            forgotemail.requestFocus()
            return
        }

        auth.sendPasswordResetEmail(forgotemail.text.toString())
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    Toast.makeText(
                        baseContext,"Email Sent.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun doLogin(){
        if(loemailtxt.text.toString().isEmpty()){
            loemailtxt.error = "Please enter Email"
            loemailtxt.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(loemailtxt.text.toString()).matches()){
            loemailtxt.error = "Please enter correct Email format"
            loemailtxt.requestFocus()
            return
        }

        if(lopasstxt.text.toString().isEmpty()) {
            lopasstxt.error = "Please enter Password"
            lopasstxt.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(loemailtxt.text.toString(), lopasstxt.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    updateUI(null)
                }
            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){

        if(currentUser != null){
            if (currentUser.isEmailVerified){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(
                    baseContext,"Please verify email address",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            Toast.makeText(
                baseContext,"Invalid Email or Password",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}
