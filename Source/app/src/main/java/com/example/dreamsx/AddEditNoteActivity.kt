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

    var predictionSelected: Boolean = false
    var predictionAnswers: String = ""
    var numberOfCountPrecQuestion: Int = 0

    //вопросы при создании сна
    lateinit var precisionYesBtn:RadioButton
    lateinit var precisionNoBtn:RadioButton
    lateinit var questionLabel:TextView
    lateinit var answerLabel:EditText
    lateinit var precionNextBtn: Button

    val predictionQuestionsList:List<String> = listOf(
        "Вы были в обычном для себя месте?",
        "Видели знакомых?",
        "3 ",
        )

    val predictionHintsList:List<String> = listOf(
        "Введите название места",
        "Введите имя знакомого",
        "3 ",
    )

    val predictionAnswerPositiveList:List<String> = listOf(
        "Был рядом мой приятель ",
        "Я в обычном для себя месте ",
        "3 ",
    )

    val predictionAnswerNegativeList:List<String> = listOf(
        "Был рядом мой приятель ",
        "Я оказался на новой для себя локации ",
        "3 ",
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        //---*******************************************************************************
        noteTitleEdit = findViewById(R.id.idEditNoteTitle)
        noteDescriptionEdit = findViewById(R.id.idEditNoteDescription)
        noteTagEdit = findViewById(R.id.idEditNoteTag)
        //noteMoodEdit = findViewById(R.id.idEditNoteDescription)

        val dreamMoodRadioGroup: RadioGroup = findViewById<RadioGroup>(R.id.moodRadioGroup)
        val precisionRadioGroup: RadioGroup = findViewById<RadioGroup>(R.id.precisionRadioGroup)
        precisionYesBtn = findViewById<RadioButton>(R.id.idRadioBtnYes)
        precisionNoBtn= findViewById<RadioButton>(R.id.idRadioBtnNo)
        questionLabel = findViewById<TextView>(R.id.idQuestionLabel)
        answerLabel = findViewById<EditText>(R.id.idEditPrcsAnswer)
        answerLabel.isActivated = false

        precionNextBtn = findViewById<Button>(R.id.idBtnNextPrecision)

        val moodMiddleBtn:RadioButton = findViewById<RadioButton>(R.id.radio_middle)

        precisionNoBtn.isChecked = true
        precionNextBtn.isEnabled = false
        answerLabel.isEnabled = false

        moodMiddleBtn.isChecked = true
        noteMoodEdit = DreamMood.MIDDLE

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

        //Заполнение предсказания
        // Get radio group selected item using on checked change listener
        precisionRadioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)

                if (checkedId == R.id.idRadioBtnYes){

                    turnOnPredictionBlock(true)

                    Toast.makeText(this, "*checkTRUE", Toast.LENGTH_LONG).show()
                    answerLabel.isEnabled = true
                }
                else if (checkedId == R.id.idRadioBtnNo){
                    turnOnPredictionBlock(false)
                    Toast.makeText(this, "*checkFALSE", Toast.LENGTH_LONG).show()
                }
            })



        //Кнопка Далее для следующего вопроса.
        precionNextBtn.setOnClickListener {
            NextPrediction()
        }

        //Кнопка добавления/обновления сна.
        addUpdateButton.setOnClickListener {
            //---*******************************************************************************
            val noteTitle = noteTitleEdit.text.toString()
            val noteDescription = noteDescriptionEdit.text.toString()
            val noteTag = noteTagEdit.text.toString()

            //Заполнение настроения

            val selectMoodBtn:Int = dreamMoodRadioGroup!!.checkedRadioButtonId
            val selectedMoodBtn = findViewById<RadioButton>(selectMoodBtn)

            if (selectedMoodBtn.id == R.id.radio_good){
                noteMoodEdit = DreamMood.COOL
            }
            else if (selectedMoodBtn.id == R.id.radio_middle){
                noteMoodEdit = DreamMood.MIDDLE
            }
            else if (selectedMoodBtn.id == R.id.radio_sad){
                noteMoodEdit = DreamMood.BAD
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
            }
            else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() ){
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())


                    var noteFinalDescription: String = ""

                    //Добавляем к описанию предсказание сна
                    if (predictionAnswers.isNotEmpty())
                        noteFinalDescription += predictionAnswers

                    noteFinalDescription += noteDescription // Добавляем описание сна

                    viewModel.addNote(Note(noteTitle, noteFinalDescription, currentDate, noteTag, noteMoodEdit))
                    Toast.makeText(this, "Сон добавлен...", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }

    private fun NextPrediction(){
        predictionAnswers += createPrediction(numberOfCountPrecQuestion) //Добавляем текущие предсказание к строке
        //Выключаем кн Далее если все вопросы заданы.
        if (numberOfCountPrecQuestion == predictionQuestionsList.size-1){
            precionNextBtn.isEnabled = false
            return
        }
        answerLabel.clear() //Очищаем предыдущий ввод
        numberOfCountPrecQuestion++; //Увеличиваем счетчик вопросов

        Toast.makeText(this, predictionQuestionsList.size.toString(), Toast.LENGTH_LONG).show()
    }

    private fun createPrediction(indexOfQuestion: Int) : String {
        var resStr:String = ""

        var selectedQuestion = predictionQuestionsList[indexOfQuestion]
        questionLabel.text = selectedQuestion
        answerLabel.isEnabled = true

        //открывается поле для ввода.
        answerLabel.hint = predictionHintsList[indexOfQuestion]

        var resAnswer: String
        if (predictionSelected)
            resAnswer = predictionAnswerPositiveList[indexOfQuestion]
        else
            resAnswer = predictionAnswerNegativeList[indexOfQuestion]

        resStr = resAnswer + answerLabel.text
        return resStr
    }

    var flagOnlyOnce: Boolean = true

    private fun turnOnPredictionBlock(turn: Boolean){
        predictionSelected = turn
        //Далее активации/деактивируется только на 1 вопросе
        if (numberOfCountPrecQuestion >= 1)
            flagOnlyOnce = false

        if (numberOfCountPrecQuestion==0 && predictionSelected) {
            NextPrediction()
        }

        else if(!predictionSelected && numberOfCountPrecQuestion<2 ){ // of - выключаем.
            numberOfCountPrecQuestion=0 //
            questionLabel.text=""
            answerLabel.clear()
            answerLabel.hint = ""
        }
        precionNextBtn.isEnabled = turn
        answerLabel.isEnabled = turn

    }

    private fun EditText.clear() {
        text.clear()
    }


}