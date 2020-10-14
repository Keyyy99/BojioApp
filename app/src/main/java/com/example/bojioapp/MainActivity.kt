package com.example.bojioapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import com.example.bojioapp.databinding.ActivityMainBinding

private lateinit var drawerLayout: DrawerLayout

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item->
        when(item.itemId){
            R.id.homebtn->{
                println("Home Pressed")
                startFrag(RequestFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.notibtn->{
                println("Notification Pressed")
                val intent = Intent(this,Notification::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.reqbtn->{
                println("Request Pressed")
                startFrag(AddRequestFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.chatbtn->{
                println("Chat Pressed")
                val intent = Intent(this,chat::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.profbtn->{
                println("Profile Pressed")
                startFrag(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }else->{
            startFrag(ProfileFragment())
            return@OnNavigationItemSelectedListener true
        }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        super.onPause()
        overridePendingTransition(0, 0)

        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController)

        btm_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        startFrag(RequestFragment())

    }

    private fun startFrag(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
        overridePendingTransition(0, 0)
    }

}
