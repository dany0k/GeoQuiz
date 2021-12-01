package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var isCheater = false


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveBack() {
        if (currentIndex == 0) {
            currentIndex = questionBank.size - 1
        } else {
            currentIndex--
        }
    }
}