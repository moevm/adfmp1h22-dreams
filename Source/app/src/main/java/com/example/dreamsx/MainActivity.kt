package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {

    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModal: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottom_navigation : BottomNavigationView = findViewById (R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.ic_statistics -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        notesRV = findViewById<RecyclerView>(R.id.idRVNotes)
        addFAB = findViewById<FloatingActionButton>(R.id.idFABAddNote)
        notesRV.layoutManager = LinearLayoutManager(this)

        val noteRVApapter = NoteRVApapter(this,this, this)
        notesRV.adapter = noteRVApapter

        viewModal = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModal.allNotes.observe(this, Observer{ list ->
            list?.let{
                noteRVApapter.updateList(it)
            }
        })

        addFAB.setOnClickListener{
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.notesTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteTag", note.noteTags)
        intent.putExtra("nodeID", note.id)
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteClick(note: Note) {
        viewModal.deleteNote(note)
        Toast.makeText(this, "${note.notesTitle}", Toast.LENGTH_LONG).show()
    }


}