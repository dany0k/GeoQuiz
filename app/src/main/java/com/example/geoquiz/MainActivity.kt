package com.example.geoquiz;

import android.app.Activity
import android.content.Intent
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.os.PersistableBundle
import android.text.BoringLayout
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var showResultButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        showResultButton = findViewById(R.id.show_result_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveBack()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        // Todo: Усправить, когда узнаю как!
        showResultButton.setOnClickListener {
            Toast.makeText(this, "Правильных ответов - " + quizViewModel.correctAnswers +
                    "\n" +
                    "Неправильных ответов - " + quizViewModel.incorrectAnswers, Toast.LENGTH_SHORT)
                .show()
        }
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null && data.getBooleanExtra(EXTRA_ANSWER_SHOW, false)) {
                quizViewModel.setTrueCurrentIsCheated()
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer: Boolean = quizViewModel.currentQuestionAnswer

        val messageResId: Int = when {
            quizViewModel.isAnswered -> {
                R.string.question_is_answered_toast
            }
            quizViewModel.getCurrentIsCheated() -> {
                R.string.judgment_toast
            }
            userAnswer == correctAnswer -> {
                quizViewModel.correctAnswers++
                R.string.correct_toast
            }
            else -> {
                quizViewModel.incorrectAnswers++
                R.string.incorrect_toast
            }
        }
        quizViewModel.setTrueIsAnswered()


        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}