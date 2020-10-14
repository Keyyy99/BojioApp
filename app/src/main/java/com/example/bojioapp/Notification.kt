package com.example.bojioapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.bojioapp.model.getUser
import com.example.bojioapp.model.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.notification_layout.view.*


class Notification : AppCompatActivity() {

    companion object{
        var Users: getUser? =null
        var notifications: Notification? =null
        val USER_KEY = "USER_KEY"
        val TAG = "text"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        supportActionBar?.title = "Notification"
        fetchNotification()
        recyclerview_notification.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
    }

    private fun fetchNotification(){
        val host = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/notification/$host")
         ref.addListenerForSingleValueEvent(object: ValueEventListener{
             override fun onDataChange(p0: DataSnapshot) {
                 val adapter = GroupAdapter<ViewHolder>()
                 p0.children.forEach{
                     val noti = it.getValue(Notification::class.java)
                     Log.d(TAG,it.toString())

                     if(noti != null){
                         adapter.add(NotiItem(noti))
                     }
                 }
                 recyclerview_notification.adapter = adapter

             }
             override fun onCancelled(p0: DatabaseError) {}
        })
    }




    class NotiItem(val noti:Notification): Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            val host = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().getReference("/users")
            viewHolder.itemView.textView_notification.text = noti.text

        }
        override fun getLayout(): Int {
            return R.layout.notification_layout
        }


}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.options_menu, menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
        R.id.newChat -> {
            val intent = Intent(this, NewChat::class.java)
            startActivity(intent)
            true
        }R.id.signOut->{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()

            true
        }else -> {

            return super.onOptionsItemSelected(item)
        }

    }
}
}

