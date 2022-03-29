package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {
    lateinit var noteTitleEdit : EditText
    lateinit var noteDescriptionEdit : EditText
    lateinit var noteTagEdit : EditText
    lateinit var noteMoodEdit : DreamMood

    lateinit var addUpdateButton : Button
    lateinit var viewModel: NoteViewModel
    var noteID = -1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        //---*******************************************************************************
        noteTitleEdit = findViewById(R.id.idEditNoteTitle)
        noteDescriptionEdit = findViewById(R.id.idEditNoteDescription)
        noteTagEdit = findViewById(R.id.idEditNoteTag)
        //noteMoodEdit = findViewById(R.id.idEditNoteDescription)


        addUpdateButton = findViewById(R.id.idBtnAddUpdate)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")){
            supportActionBar?.title = "Редактирование сна"
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDesc = intent.getStringExtra("noteDescription")
            val noteTags = intent.getStringExtra("noteTag")

            noteID = intent.getIntExtra("noteID", -1)
            addUpdateButton.setText("Обновить сон")

            //---*******************************************************************************
            noteTitleEdit.setText(noteTitle)
            noteDescriptionEdit.setText(noteDesc)
            noteTagEdit.setText(noteTags)


        } else  {
            supportActionBar?.title = "Создание сна"
            addUpdateButton.setText("Сохранить сон")
        }


        addUpdateButton.setOnClickListener {
            //---*******************************************************************************
            val noteTitle = noteTitleEdit.text.toString()
            val noteDescription = noteDescriptionEdit.text.toString()
            val noteTag = noteTagEdit.text.toString()

            if (noteType.equals("Edit")){
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() ){
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())

                    print("** * Updating noteTag: $noteTag")

                    val updateNote = Note(noteTitle, noteDescription, currentDate, noteTag, DreamMood.COOL)  ///////
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(this, "Сон обновлен...", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() ){
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())

                    print("** * Creating new noteTag: $noteTag")
                    viewModel.addNote(Note(noteTitle, noteDescription, currentDate, noteTag, DreamMood.COOL))   ///////////////////
                    Toast.makeText(this, "Сон добавлен...", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}