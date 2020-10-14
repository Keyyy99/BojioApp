package com.example.bojioapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bojioapp.model.getUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_chat.*
import kotlinx.android.synthetic.main.user_row.view.*

class NewChat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)
        super.onPause()
        overridePendingTransition(0, 0)
        supportActionBar?.title = "Select User"
        fetchUsers()
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                   p0.children.forEach{
                     Log.d("NewChat",it.toString())
                     val user =it.getValue(getUser::class.java)
                     if(user != null) {
                         if(user.uid != FirebaseAuth.getInstance().uid) {
                             adapter.add(UserItem(user))
                         }
                     }
                }
                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem
                    val intent = Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)
                    finish()
                }
                recycleview_newchat.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}

class UserItem(val user: getUser): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
      viewHolder.itemView.username_text_newchat.text = user.name
      Picasso.get().load(user.photo).into(viewHolder.itemView.imageView_newchat)
    }
    override fun getLayout(): Int {
        return R.layout.user_row
    }
}
