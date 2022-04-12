package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import  java.io.Serializable

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {

    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModal: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Дневник снов"

        val bottom_navigation : BottomNavigationView = findViewById (R.id.bottom_navigation)


        notesRV = findViewById<RecyclerView>(R.id.idRVNotes)
        addFAB = findViewById<FloatingActionButton>(R.id.idFABAddNote)
        notesRV.layoutManager = LinearLayoutManager(this)

        val noteRVApapter = NoteRVApapter(this,this, this)
        notesRV.adapter = noteRVApapter

        // Получение количества снов
        var countOfDreams: Int = 0
        var countOfPositiveDreams : Int = 0
        var countOfMiddleDreams : Int = 0
        var countOfNegativeDreams : Int = 0
        var listOfAllNotes : List<Note> = listOf()

        viewModal = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModal.allNotes.observe(this, Observer{ list ->
            list?.let{
                noteRVApapter.updateList(it)
            }
            countOfDreams = noteRVApapter.getCountOfDreams(list)
            countOfPositiveDreams = noteRVApapter.getCountOfDreams(list.filter { it.mood == DreamMood.COOL })
            countOfMiddleDreams = noteRVApapter.getCountOfDreams(list.filter { it.mood == DreamMood.MIDDLE })
            countOfNegativeDreams = noteRVApapter.getCountOfDreams(list.filter { it.mood == DreamMood.BAD })

            //listOfAllTags = noteRVApapter.getAllTags(list.filter { it.noteTags != "" })
            listOfAllNotes = noteRVApapter.getAllNotes()
        })

        addFAB.setOnClickListener{
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        val noteX = Note("Title", "that is title", "12-12", "#samara", DreamMood.COOL)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.ic_statistics -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    intent.putExtra("listOfAllNotes", listOfAllNotes as Serializable)
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("note", note)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteID", note.id)
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteClick(note: Note) {
        viewModal.deleteNote(note)
        Toast.makeText(this, "Сон '${note.notesTitle}' удалён.", Toast.LENGTH_LONG).show()
    }


}