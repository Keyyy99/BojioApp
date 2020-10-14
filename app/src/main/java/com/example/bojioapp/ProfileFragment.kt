package com.example.bojioapp


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    //database
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var mImageStorage: FirebaseStorage? = null

    private var Name: TextView? = null
    private var Age: TextView? = null
    private var Gender: TextView? = null
    private var Phone: TextView? = null
    private var Email: TextView? = null
    private var Intro: TextView? = null
    private var Photo: CircleImageView? = null
    lateinit var editBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (context as AppCompatActivity).supportActionBar!!.title = "Profile"
        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_profile,container,false)

        editBtn = root.findViewById(R.id.editbtn) as Button

        editBtn.setOnClickListener{
            val intent = Intent (activity, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }


        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("users")
        mImageStorage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()

        Name = root.findViewById(R.id.profname) as TextView
        Age = root.findViewById(R.id.profAgetxt) as TextView
        Gender = root.findViewById(R.id.profGentxt) as TextView
        Phone = root.findViewById(R.id.profPhonetxt) as TextView
        Email = root.findViewById(R.id.profEmailtxt) as TextView
        Intro = root.findViewById(R.id.profIntrotxt) as TextView
        Photo = root.findViewById(R.id.profImg) as CircleImageView


        return root
    }

    override fun onStart(){
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        Email!!.text = mUser.email

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Name!!.text = snapshot.child("name").value as? String
                Age!!.text = snapshot.child("age").value as? String
                Gender!!.text = snapshot.child("gender").value as? String
                Phone!!.text = snapshot.child("phone").value as? String
                Intro!!.text = snapshot.child("intro").value as? String

                val directory = File("https://firebasestorage.googleapis.com/v0/b/bojioapp-bf834.appspot.com/o/nopic%2Fprofile.png?alt=media&token=ee7901b5-e12e-411b-9d5a-b186f32c3e5e")

                try {
                    if ((snapshot.child("photo").value as? String)==null) {
                        Picasso.get().load(directory).into(profImg)
                    } else {
                        Picasso.get().load(snapshot.child("photo").value as? String).into(profImg)
                    }
                }catch(ex: Exception){
                    }

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
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
