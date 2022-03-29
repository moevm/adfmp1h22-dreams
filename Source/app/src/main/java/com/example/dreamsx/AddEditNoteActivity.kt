package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
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


    var precisionSelected: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        //---*******************************************************************************
        noteTitleEdit = findViewById(R.id.idEditNoteTitle)
        noteDescriptionEdit = findViewById(R.id.idEditNoteDescription)
        noteTagEdit = findViewById(R.id.idEditNoteTag)
        //noteMoodEdit = findViewById(R.id.idEditNoteDescription)

        val precisionRadioGroup: RadioGroup = findViewById<RadioGroup>(R.id.precisionRadioGroup)
        val precisionYesBtn:RadioButton = findViewById<RadioButton>(R.id.idRadioBtnYes)

        val precisionNoBtn:RadioButton = findViewById<RadioButton>(R.id.idRadioBtnNo)
        val precionNextBtn: Button = findViewById<Button>(R.id.idBtnNextPrecision)

        val moodMiddleBtn:RadioButton = findViewById<RadioButton>(R.id.radio_middle)

        precisionNoBtn.isChecked = true
        precionNextBtn.isEnabled = false

        moodMiddleBtn.isChecked = true
        noteMoodEdit = DreamMood.MIDDLE

        val dreamMoodRadioGroup: RadioGroup = findViewById<RadioGroup>(R.id.moodRadioGroup)

        addUpdateButton = findViewById(R.id.idBtnAddUpdate)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        val noteType = intent.getStringExtra("noteType")

        //Заполнение UI из БД в случае редактирования
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


            //Заполнение предсказания
            val selectPrecionBtn:Int = precisionRadioGroup!!.checkedRadioButtonId
            val selectedPrecBtn = findViewById<RadioButton>(selectPrecionBtn)

            if (selectedPrecBtn.id == R.id.idRadioBtnYes){
                precisionSelected = true
                precionNextBtn.isEnabled = true
                //Toast.makeText(this, "checkTRUE", Toast.LENGTH_LONG).show()
            }
            else if (selectedPrecBtn.id == R.id.idRadioBtnNo){
                precisionSelected = false
                precionNextBtn.isEnabled = false
                //Toast.makeText(this, "checkFALSE", Toast.LENGTH_LONG).show()
            }

            //Заполнение настроения

            val selectMoodBtn:Int = dreamMoodRadioGroup!!.checkedRadioButtonId
            val selectedMoodBtn = findViewById<RadioButton>(selectMoodBtn)

            if (selectedMoodBtn.id == R.id.radio_good){
                noteMoodEdit = DreamMood.COOL
                //Toast.makeText(this, "GOOD!!!!", Toast.LENGTH_LONG).show()
            }
            else if (selectedMoodBtn.id == R.id.radio_middle){
                noteMoodEdit = DreamMood.MIDDLE
                //Toast.makeText(this, "MIDDLE!!!!", Toast.LENGTH_LONG).show()
            }
            else if (selectedMoodBtn.id == R.id.radio_sad){
                noteMoodEdit = DreamMood.BAD
                //Toast.makeText(this, "SAD!!!!!", Toast.LENGTH_LONG).show()
            }

            if (noteType.equals("Edit")){
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() ){
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    precionNextBtn.isEnabled = false
                    print("** * Updating noteTag: $noteTag")
                    val updateNote = Note(noteTitle, noteDescription, currentDate, noteTag, noteMoodEdit)  ///////
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(this, "Сон обновлен...", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() ){
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())

                    print("** * Creating new noteTag: $noteTag")
                    viewModel.addNote(Note(noteTitle, noteDescription, currentDate, noteTag, noteMoodEdit))   ///////////////////
                    Toast.makeText(this, "Сон добавлен...", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}