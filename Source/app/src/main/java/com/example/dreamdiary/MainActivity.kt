package com.example.dreamdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamdiary.fragments.DashboardFragment
import com.example.dreamdiary.fragments.SettingsFragment
import com.example.dreamdiary.fragments.StatisticsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val dashboardFragment = DashboardFragment(this)
    private val settingsFragment = SettingsFragment()
    private val statisticsFragment = StatisticsFragment()
    lateinit var notesRV: RecyclerView //
    lateinit var addFAB: FloatingActionButton //
    lateinit var viewModal: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val secondActivityBtn: Button = findViewById (R.id.secondActivityBtn)

        secondActivityBtn.setOnClickListener{
            val intent = Intent(this, SettingsActivty::class.java)
            startActivity(intent)
        }
        replaceFragment(dashboardFragment )

        //notesRV.layoutManager = LinearLayoutManager(this)

        //val noteRVApapter = NoteRVApapter(this,this, this)

        val bottom_navigation : BottomNavigationView = findViewById (R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_dashboard ->{
                    replaceFragment(dashboardFragment);
//                    val intent = Intent(this, SettingsActivty::class.java)
//                    startActivity(intent)
                }
                R.id.ic_settings -> replaceFragment(settingsFragment)
                R.id.ic_statistics -> replaceFragment(statisticsFragment)
            }
            true
        }

//        val noteRVApapter = NoteRVApapter(this,this, this)
//
//        viewModal = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
//        viewModal.allNotes.observe(this, Observer{list ->
//            list?.let{
//                noteRVApapter.updateList(it)
//            }
//        })

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