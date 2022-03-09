package com.example.dreamdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.ButtonBarLayout
import androidx.fragment.app.Fragment
import com.example.dreamdiary.fragments.DashboardFragment
import com.example.dreamdiary.fragments.SettingsFragment
import com.example.dreamdiary.fragments.StatisticsFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val dashboardFragment = DashboardFragment()
    private val settingsFragment = SettingsFragment()
    private val statisticsFragment = StatisticsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val secondActivityBtn: Button = findViewById (R.id.secondActivityBtn)

        secondActivityBtn.setOnClickListener{
            val intent = Intent(this, SettingsActivty::class.java)
            startActivity(intent)
        }


        replaceFragment(dashboardFragment )

        val bottom_navigation : BottomNavigationView = findViewById (R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_dashboard -> replaceFragment(dashboardFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
                R.id.ic_statistics -> replaceFragment(statisticsFragment)
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment){
        if (fragment!=null){
            val trasaction = supportFragmentManager.beginTransaction()
            trasaction.replace(R.id.fragment_container, fragment)
            trasaction.commit()
        }
    }

}