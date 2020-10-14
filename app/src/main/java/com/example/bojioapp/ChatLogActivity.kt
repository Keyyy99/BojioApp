package com.example.bojioapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bojioapp.model.ChatMessage
import com.example.bojioapp.model.getUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<ViewHolder>()

    var fromUser:getUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        super.onPause()
        overridePendingTransition(0, 0)
        recycleview_chatlog.adapter = adapter
        fromUser = intent.getParcelableExtra<getUser>(NewChat.USER_KEY)
        supportActionBar?.title = fromUser?.name

        listenForMessage()

        sendbtn_chatlog.setOnClickListener{
            Log.d(TAG,"Attempt to send message.....")
            performSendMessage()
        }
        }

    private fun listenForMessage(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = fromUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if(chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = chat.currentUser ?: return
                        adapter.add(ChatToItem(chatMessage.text,currentUser))
                    }else {
                        adapter.add(ChatFromItem(chatMessage.text,fromUser!!))
                    }
                }
                recycleview_chatlog.scrollToPosition((adapter.itemCount -1))
            }
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    private fun performSendMessage(){
        //send message to firebase
        val text = editText_chatlog.text.trim().toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<getUser>(NewChat.USER_KEY)
        val toId= user.uid

        if(fromId == null) return


        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toref = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(ref.key!!, fromId, toId, System.currentTimeMillis()/1000 , text)
        if(text.isNotEmpty()) {
            ref.setValue(chatMessage).addOnSuccessListener {
                Log.d(TAG, "Save chat message :${ref.key}")
                editText_chatlog.text.clear()
                recycleview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }
            toref.setValue(chatMessage)

            val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
            latestMessageRef.setValue(chatMessage)

            val latestMessageToRef =FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
            latestMessageToRef.setValue(chatMessage)

        }

    }

}




class ChatFromItem(val text:String,val user: getUser):Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //change text
        viewHolder.itemView.textView_fromRow.text = text

        val uri = user.photo
        val targetImageView = viewHolder.itemView.imageView_fromRow
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {

        return R.layout.chat_from_row
    }
}

class ChatToItem(val text:String,val user: getUser):Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //change text
        viewHolder.itemView.textView_toRow.text = text

        val uri = user.photo
        val targetImageView = viewHolder.itemView.imageView_toRow
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {

        return R.layout.chat_to_row
    }
}