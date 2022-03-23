package com.example.dreamdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.ButtonBarLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamdiary.fragments.DashboardFragment
import com.example.dreamdiary.fragments.SettingsFragment
import com.example.dreamdiary.fragments.StatisticsFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val dashboardFragment = DashboardFragment()
    private val settingsFragment = SettingsFragment()
    private val statisticsFragment = StatisticsFragment()
    lateinit var notesRV: RecyclerView //
    lateinit var addFAB: FloatingActionButton //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val secondActivityBtn: Button = findViewById (R.id.secondActivityBtn)

        secondActivityBtn.setOnClickListener{
            val intent = Intent(this, SettingsActivty::class.java)
            startActivity(intent)
        }
        replaceFragment(dashboardFragment )
//        val actionBar = supportActionBar
//        actionBar!!.title = "Settings Activity"

        notesRV.layoutManager = LinearLayoutManager(this)



        val bottom_navigation : BottomNavigationView = findViewById (R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_dashboard -> replaceFragment(dashboardFragment);

                R.id.ic_settings -> replaceFragment(settingsFragment)
                R.id.ic_statistics -> replaceFragment(statisticsFragment)
            }
            true
        }


        //notesRV = findViewById(R.id.idRVNotes)

    }


    private fun replaceFragment(fragment: Fragment){
        if (fragment!=null){
            val trasaction = supportFragmentManager.beginTransaction()
            trasaction.replace(R.id.fragment_container, fragment)
            trasaction.commit()
        }
    }

}