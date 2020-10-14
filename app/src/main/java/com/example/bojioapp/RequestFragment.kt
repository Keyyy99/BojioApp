package com.example.bojioapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_request.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */

    lateinit var mRecyclerView: RecyclerView
    lateinit var ref:DatabaseReference
    lateinit var query:Query
    lateinit var query2:Query
    lateinit var query3:Query
    lateinit var query4:Query
    lateinit var query5:Query

class RequestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (context as AppCompatActivity).supportActionBar!!.title = "BoJio"
        setHasOptionsMenu(true)

        val rootView = inflater.inflate(R.layout.fragment_request, container, false)

        mRecyclerView = rootView.findViewById(R.id.request_list) as RecyclerView

        val categorySpin = rootView.findViewById<Spinner>(R.id.category_spinner)

        ref = FirebaseDatabase.getInstance().getReference("requests")
        query=ref.orderByChild("category").equalTo("Sports")
        query2=ref.orderByChild("category").equalTo("Entertainments")
        query3=ref.orderByChild("category").equalTo("Games")
        query4=ref.orderByChild("category").equalTo("Leisure")
        query5=ref.orderByChild("category").equalTo("Education")


        mRecyclerView.layoutManager = LinearLayoutManager(context)

        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        categorySpin.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                var choose = categorySpin.getSelectedItem().toString()
                onLoadData(choose)
            }
        })

        return rootView
    }

    fun onLoadData(choose:String){

        try {
            if(choose=="All") {
                val option = FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(ref, Request::class.java)
                    .build()

                val firebaseRecyclerAdapter =
                    object : FirebaseRecyclerAdapter<Request, RequestViewHolder>(option) {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): RequestViewHolder {
                            val itemView = LayoutInflater.from(context)
                                .inflate(R.layout.request_item_list, parent, false)
                            return RequestViewHolder(itemView)
                        }

                        override fun onBindViewHolder(
                            holder: RequestViewHolder,
                            position: Int,
                            model: Request
                        ) {
                            var refid: String = getRef(position).key.toString()

                            ref.child(refid).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    holder.mtitle.setText(model.title)
                                    holder.mdate.setText(model.date)
                                    holder.mtime.setText(model.time)
                                    holder.mlocation.setText(model.location)
                                    holder.mcategory.setText(model.category)

                                    holder.itemView.setOnClickListener {
                                        val intent = Intent(activity, ViewRequest::class.java)
                                        intent.putExtra("Firebase_Rid", model.rid)
                                        intent.putExtra("Firebase_Host", model.host)
                                        intent.putExtra("Firebase_Title", model.title)
                                        intent.putExtra("Firebase_Date", model.date)
                                        intent.putExtra("Firebase_Time", model.time)
                                        intent.putExtra("Firebase_Location", model.location)
                                        intent.putExtra("Firebase_Category", model.category)
                                        intent.putExtra("Firebase_Description", model.description)
                                        intent.putExtra("Firebase_Participant", model.participantNum
                                        )
                                        view?.context?.startActivity(intent)
                                    }

                                }

                            })
                        }

                    }

                mRecyclerView.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
            }else if(choose=="Sports") {
                val option = FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(query, Request::class.java)
                    .build()

                val firebaseRecyclerAdapter =
                    object : FirebaseRecyclerAdapter<Request, RequestViewHolder>(option) {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): RequestViewHolder {
                            val itemView = LayoutInflater.from(context)
                                .inflate(R.layout.request_item_list, parent, false)
                            return RequestViewHolder(itemView)
                        }

                        override fun onBindViewHolder(
                            holder: RequestViewHolder,
                            position: Int,
                            model: Request
                        ) {
                            var refid: String = getRef(position).key.toString()

                            ref.child(refid).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    holder.mtitle.setText(model.title)
                                    holder.mdate.setText(model.date)
                                    holder.mtime.setText(model.time)
                                    holder.mlocation.setText(model.location)
                                    holder.mcategory.setText(model.category)

                                    holder.itemView.setOnClickListener {
                                        val intent = Intent(activity, ViewRequest::class.java)
                                        intent.putExtra("Firebase_Rid", model.rid)
                                        intent.putExtra("Firebase_Host", model.host)
                                        intent.putExtra("Firebase_Title", model.title)
                                        intent.putExtra("Firebase_Date", model.date)
                                        intent.putExtra("Firebase_Time", model.time)
                                        intent.putExtra("Firebase_Location", model.location)
                                        intent.putExtra("Firebase_Category", model.category)
                                        intent.putExtra("Firebase_Description", model.description)
                                        intent.putExtra("Firebase_Participant", model.participantNum
                                        )
                                        view?.context?.startActivity(intent)
                                    }

                                }

                            })
                        }

                    }

                mRecyclerView.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
            }else if(choose=="Entertainments") {
                val option = FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(query2, Request::class.java)
                    .build()

                val firebaseRecyclerAdapter =
                    object : FirebaseRecyclerAdapter<Request, RequestViewHolder>(option) {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): RequestViewHolder {
                            val itemView = LayoutInflater.from(context)
                                .inflate(R.layout.request_item_list, parent, false)
                            return RequestViewHolder(itemView)
                        }

                        override fun onBindViewHolder(
                            holder: RequestViewHolder,
                            position: Int,
                            model: Request
                        ) {
                            var refid: String = getRef(position).key.toString()

                            ref.child(refid).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    holder.mtitle.setText(model.title)
                                    holder.mdate.setText(model.date)
                                    holder.mtime.setText(model.time)
                                    holder.mlocation.setText(model.location)
                                    holder.mcategory.setText(model.category)

                                    holder.itemView.setOnClickListener {
                                        val intent = Intent(activity, ViewRequest::class.java)
                                        intent.putExtra("Firebase_Rid", model.rid)
                                        intent.putExtra("Firebase_Host", model.host)
                                        intent.putExtra("Firebase_Title", model.title)
                                        intent.putExtra("Firebase_Date", model.date)
                                        intent.putExtra("Firebase_Time", model.time)
                                        intent.putExtra("Firebase_Location", model.location)
                                        intent.putExtra("Firebase_Category", model.category)
                                        intent.putExtra("Firebase_Description", model.description)
                                        intent.putExtra("Firebase_Participant", model.participantNum
                                        )
                                        view?.context?.startActivity(intent)
                                    }

                                }

                            })
                        }

                    }

                mRecyclerView.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
            }else if(choose=="Games") {
                val option = FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(query3, Request::class.java)
                    .build()

                val firebaseRecyclerAdapter =
                    object : FirebaseRecyclerAdapter<Request, RequestViewHolder>(option) {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): RequestViewHolder {
                            val itemView = LayoutInflater.from(context)
                                .inflate(R.layout.request_item_list, parent, false)
                            return RequestViewHolder(itemView)
                        }

                        override fun onBindViewHolder(
                            holder: RequestViewHolder,
                            position: Int,
                            model: Request
                        ) {
                            var refid: String = getRef(position).key.toString()

                            ref.child(refid).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    holder.mtitle.setText(model.title)
                                    holder.mdate.setText(model.date)
                                    holder.mtime.setText(model.time)
                                    holder.mlocation.setText(model.location)
                                    holder.mcategory.setText(model.category)

                                    holder.itemView.setOnClickListener {
                                        val intent = Intent(activity, ViewRequest::class.java)
                                        intent.putExtra("Firebase_Rid", model.rid)
                                        intent.putExtra("Firebase_Host", model.host)
                                        intent.putExtra("Firebase_Title", model.title)
                                        intent.putExtra("Firebase_Date", model.date)
                                        intent.putExtra("Firebase_Time", model.time)
                                        intent.putExtra("Firebase_Location", model.location)
                                        intent.putExtra("Firebase_Category", model.category)
                                        intent.putExtra("Firebase_Description", model.description)
                                        intent.putExtra("Firebase_Participant", model.participantNum
                                        )
                                        view?.context?.startActivity(intent)
                                    }

                                }

                            })
                        }

                    }

                mRecyclerView.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
            }else if(choose=="Leisure") {
                val option = FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(query4, Request::class.java)
                    .build()

                val firebaseRecyclerAdapter =
                    object : FirebaseRecyclerAdapter<Request, RequestViewHolder>(option) {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): RequestViewHolder {
                            val itemView = LayoutInflater.from(context)
                                .inflate(R.layout.request_item_list, parent, false)
                            return RequestViewHolder(itemView)
                        }

                        override fun onBindViewHolder(
                            holder: RequestViewHolder,
                            position: Int,
                            model: Request
                        ) {
                            var refid: String = getRef(position).key.toString()

                            ref.child(refid).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    holder.mtitle.setText(model.title)
                                    holder.mdate.setText(model.date)
                                    holder.mtime.setText(model.time)
                                    holder.mlocation.setText(model.location)
                                    holder.mcategory.setText(model.category)

                                    holder.itemView.setOnClickListener {
                                        val intent = Intent(activity, ViewRequest::class.java)
                                        intent.putExtra("Firebase_Rid", model.rid)
                                        intent.putExtra("Firebase_Host", model.host)
                                        intent.putExtra("Firebase_Title", model.title)
                                        intent.putExtra("Firebase_Date", model.date)
                                        intent.putExtra("Firebase_Time", model.time)
                                        intent.putExtra("Firebase_Location", model.location)
                                        intent.putExtra("Firebase_Category", model.category)
                                        intent.putExtra("Firebase_Description", model.description)
                                        intent.putExtra("Firebase_Participant", model.participantNum
                                        )
                                        view?.context?.startActivity(intent)
                                    }

                                }

                            })
                        }

                    }

                mRecyclerView.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
            }else if(choose=="Education") {
                val option = FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(query5, Request::class.java)
                    .build()

                val firebaseRecyclerAdapter =
                    object : FirebaseRecyclerAdapter<Request, RequestViewHolder>(option) {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): RequestViewHolder {
                            val itemView = LayoutInflater.from(context)
                                .inflate(R.layout.request_item_list, parent, false)
                            return RequestViewHolder(itemView)
                        }

                        override fun onBindViewHolder(
                            holder: RequestViewHolder,
                            position: Int,
                            model: Request
                        ) {
                            var refid: String = getRef(position).key.toString()

                            ref.child(refid).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    holder.mtitle.setText(model.title)
                                    holder.mdate.setText(model.date)
                                    holder.mtime.setText(model.time)
                                    holder.mlocation.setText(model.location)
                                    holder.mcategory.setText(model.category)

                                    holder.itemView.setOnClickListener {
                                        val intent = Intent(activity, ViewRequest::class.java)
                                        intent.putExtra("Firebase_Rid", model.rid)
                                        intent.putExtra("Firebase_Host", model.host)
                                        intent.putExtra("Firebase_Title", model.title)
                                        intent.putExtra("Firebase_Date", model.date)
                                        intent.putExtra("Firebase_Time", model.time)
                                        intent.putExtra("Firebase_Location", model.location)
                                        intent.putExtra("Firebase_Category", model.category)
                                        intent.putExtra("Firebase_Description", model.description)
                                        intent.putExtra("Firebase_Participant", model.participantNum
                                        )
                                        view?.context?.startActivity(intent)
                                    }

                                }

                            })
                        }

                    }

                mRecyclerView.adapter = firebaseRecyclerAdapter
                firebaseRecyclerAdapter.startListening()
            }
        }catch(ex: Exception){
            Toast.makeText(context,ex.message, Toast.LENGTH_LONG).show()
        }
    }

    class RequestViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var  mtitle:TextView = itemView!!.findViewById<TextView>(R.id.titleView)
        var  mdate:TextView = itemView!!.findViewById<TextView>(R.id.dateView)
        var  mtime:TextView = itemView!!.findViewById<TextView>(R.id.timeView)
        var  mlocation:TextView = itemView!!.findViewById<TextView>(R.id.locationView)
        var  mcategory:TextView = itemView!!.findViewById<TextView>(R.id.categoryView)
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

