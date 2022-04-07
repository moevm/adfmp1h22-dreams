package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*


class AddEditNoteActivity() : AppCompatActivity() {
    lateinit var noteTitleEdit : EditText
    lateinit var noteDescriptionEdit : EditText
    lateinit var noteTagEdit : EditText
    lateinit var noteMoodEdit : DreamMood

    lateinit var addUpdateButton : Button

    lateinit var viewModel: NoteViewModel
    var noteID = -1;

    var currentQuestion: Int = -1


    var isPredictionYesSelected: Boolean = false
    var predictionAnswers: String = ""


    //вопросы при создании сна
    lateinit var precisionYesBtn:RadioButton
    lateinit var precisionNoBtn:RadioButton
    lateinit var questionLabel:TextView
    lateinit var answerLabel:EditText //
    lateinit var precionNextBtn: Button

    private val predictionQuestionsList:List<String> = listOf(
        "Вы были в обычном для себя месте?",
        "Видели знакомых?",
        "Куда вы направлялись?",
        )

    private val predictionHintsList:List<String> = listOf(
        "Введите название места",
        "Введите имя знакомого",
        "Введите вашу цель",
    )

    private val predictionAnswerPositiveList:List<String> = listOf(
        "Находился в обычном для себя месте ",
        "Был рядом мой приятель ",
        "Я шел по направлению к ",
    )

    private val predictionAnswerNegativeList:List<String> = listOf(
        "Я оказался на новой для себя локации ",
        "Знакомых рядом не было ",
        "В момент сна я не передвигался ",
    )
    
    var totalCountOfQuestions: Int = predictionQuestionsList.size


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        noteTitleEdit = findViewById(R.id.idEditNoteTitle)
        noteDescriptionEdit = findViewById(R.id.idEditNoteDescription)
        noteTagEdit = findViewById(R.id.idEditNoteTag)

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
                    isPredictionYesSelected = true

                    //на первом вопросе вывестти блок предсказания
                    if (currentQuestion == -1) {
                        currentQuestion++;
                        turnOnPredictionBlock(true)
                        showQuestion()
                        precionNextBtn.isEnabled = true
                    }

                }
                else if (checkedId == R.id.idRadioBtnNo){
                    isPredictionYesSelected = false
                }
            })


        //Кнопка Далее для следующего вопроса.
        precionNextBtn.setOnClickListener {
            next()
        }

        //Кнопка добавления/обновления сна.
        addUpdateButton.setOnClickListener {

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




    // Сключение/выключение полей для ввода в зависимости от Да/Нет пользователя.
    private fun turnOnPredictionBlock(turn: Boolean){
        //выключение поля для ввода
        answerLabel.isEnabled = turn
        answerLabel.isActivated = true
        if (!turn){ // если Нет - то подсказка и выпрос не выводятся
            answerLabel.clear()
            answerLabel.hint = ""
        }
    }


    private fun next(){
        //заполняем ответ на предыдущий вопос.
        predictionAnswers += getAnswer() + answerLabel.text + ". "
        currentQuestion++;
        answerLabel.clear()

        /// Вопросы закоснчены..
        if (currentQuestion >= totalCountOfQuestions){
            precionNextBtn.isEnabled = false //Выключаем кнопку на последнем вопросе
            turnOnPredictionBlock(false)
            precisionYesBtn.isEnabled = false
            precisionNoBtn.isEnabled = false
            return
        }
        else{
            precionNextBtn.isEnabled = true

            showQuestion() //Выводим вопрос.

            // Последнйи вопрос.
            if (currentQuestion+1 == totalCountOfQuestions){
                precionNextBtn.text = "Ок"
            }
        }
    }


    private fun showQuestion(){
        questionLabel.text = predictionQuestionsList[currentQuestion]
        answerLabel.hint = predictionHintsList[currentQuestion]
    }

    private fun getAnswer() : String {

        var resAnswer : String = if (isPredictionYesSelected)
            predictionAnswerPositiveList[currentQuestion]
        else
            predictionAnswerNegativeList[currentQuestion]
        return resAnswer
    }

    private fun EditText.clear() {
        text.clear()
    }
}