package com.example.bojioapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.bojioapp.model.ChatMessage
import com.example.bojioapp.model.getUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.personalchat_layout.view.*
import kotlin.collections.HashMap


class chat : AppCompatActivity() {

    companion object{
        var currentUser: getUser? =null
        val TAG = "LatestMesssage"

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        super.onPause()
        overridePendingTransition(0, 0)

        supportActionBar?.title = "Chat"

        recycleview_chat.adapter = adapter
        recycleview_chat.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        //set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this,ChatLogActivity::class.java)
            val row = item as latestMessages
            row.chatPartnerUser
            intent.putExtra(NewChat.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }
        listenForLatestMessages()
        fetchCurrentUser()

    }



    class latestMessages(val chatMessage: ChatMessage): Item<ViewHolder>(){
        var chatPartnerUser: getUser? = null
        override fun bind(viewHolder: ViewHolder, position: Int) {
           viewHolder.itemView.chat_latestmsg.text = chatMessage.text

           val chatPartnerId:String
            if(chatMessage.fromId == FirebaseAuth.getInstance().uid)
            {
                chatPartnerId = chatMessage.toId
            }else{
                chatPartnerId = chatMessage.fromId
            }

            val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser = p0.getValue(getUser::class.java)
                    viewHolder.itemView.username_latestmsg.text = chatPartnerUser?.name
                    val targetImageView = viewHolder.itemView.imageView_latestmsg
                    Picasso.get().load(chatPartnerUser?.photo).into(targetImageView)
                }
                override fun onCancelled(p0: DatabaseError) {

                }

            })

           viewHolder.itemView.username_latestmsg.text = chatMessage.toId

        }

        override fun getLayout(): Int {

            return R.layout.personalchat_layout
        }
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()
    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(latestMessages(it))
        }
    }

    private fun listenForLatestMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }
    val adapter = GroupAdapter<ViewHolder>()


    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(getUser::class.java)

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
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
