package com.example.dreamsx

import android.annotation.SuppressLint
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
    private lateinit var precisionYesBtn:RadioButton
    private lateinit var precisionNoBtn:RadioButton
    private lateinit var questionLabel:TextView
    private lateinit var answerLabel:EditText
    private lateinit var precionNextBtn: Button


    //Кнопки настроения:
    private lateinit var moodCool: RadioButton
    private lateinit var moodMiddle: RadioButton
    private lateinit var moodBad: RadioButton

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


    @SuppressLint("SimpleDateFormat")
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
        moodCool = findViewById<RadioButton>(R.id.radio_good)
        moodMiddle = findViewById<RadioButton>(R.id.radio_middle)
        moodBad = findViewById<RadioButton>(R.id.radio_sad)
        answerLabel.isActivated = false

        precionNextBtn = findViewById<Button>(R.id.idBtnNextPrecision)
        precisionNoBtn.isChecked = true
        precionNextBtn.isEnabled = false

        moodMiddle.isChecked = true
        noteMoodEdit = DreamMood.MIDDLE

        addUpdateButton = findViewById(R.id.idBtnAddUpdate)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        val noteType = intent.getStringExtra("noteType")



        //Заполнение UI из БД в случае редактирования
        if (noteType.equals("Edit")){
            supportActionBar?.title = "Редактирование сна"
            val note: Note? = intent.getSerializableExtra("note") as? Note    // если нет значения с таким ключом, то photo == null

            noteID = intent.getIntExtra("noteID", -1)
            addUpdateButton.setText("Обновить сон")

            noteTitleEdit.setText(note?.notesTitle)
            noteDescriptionEdit.setText(note?.noteDescription)
            noteDescriptionEdit.setText(note?.noteDescription)
            noteTagEdit.setText(note?.noteTags)
            var x = note!!.mood
            noteMoodEdit = note?.mood!!

            when (noteMoodEdit) {
                DreamMood.COOL -> {
                    moodCool.isChecked = true
                }
                DreamMood.MIDDLE -> {
                    moodMiddle.isChecked = true
                }
                DreamMood.BAD-> {
                    moodBad.isChecked = true
                }
            }
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

            when (selectedMoodBtn.id) {
                R.id.radio_good -> {
                    noteMoodEdit = DreamMood.COOL
                }
                R.id.radio_middle -> {
                    noteMoodEdit = DreamMood.MIDDLE
                }
                R.id.radio_sad -> {
                    noteMoodEdit = DreamMood.BAD
                }
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            if (noteType.equals("Edit")){
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() ){
                    val currentDate:String = sdf.format(Date())
                    precionNextBtn.isEnabled = false
                    val updateNote = Note(noteTitle, noteDescription, currentDate, noteTag, noteMoodEdit)
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(this, "Сон обновлен...", Toast.LENGTH_LONG).show()
                }
            }
            else { // Create Dream
                initPredictionBlock()
                if (noteTitle.isNotEmpty() && (noteDescription.isNotEmpty() || predictionAnswers.isNotEmpty())){
                    val currentDate:String = sdf.format(Date())
                    var noteFinalDescription: String = ""

                    //Добавляем к описанию предсказание сна
                    if (predictionAnswers.isNotEmpty())
                        noteFinalDescription += predictionAnswers

                    noteFinalDescription += noteDescription // Добавляем описание сна

                    viewModel.addNote(Note(noteTitle, noteFinalDescription, currentDate, noteTag, noteMoodEdit))
                    Toast.makeText(this, "Сон добавлен...", Toast.LENGTH_LONG).show()
                }
                else    { //noteTitle is Empty.
                    Toast.makeText(this, "Заголовок сна и описание должны быть заполнены.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }

    //Вывод первого вопроса.
    private fun initPredictionBlock(){
        currentQuestion++;
        turnOnPredictionBlock(true)
        showQuestion()
        precionNextBtn.isEnabled = true
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

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        this.finish()
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