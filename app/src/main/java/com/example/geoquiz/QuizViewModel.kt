package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true, isAnswered = false, isCheated = false),
        Question(R.string.question_oceans, true, isAnswered = false, isCheated = false),
        Question(R.string.question_mideast, false, isAnswered = false, isCheated = false),
        Question(R.string.question_africa, false, isAnswered = false, isCheated = false),
        Question(R.string.question_americas, true, isAnswered = false, isCheated = false),
        Question(R.string.question_asia, true, isAnswered = false, isCheated = false)
    )

    var currentIndex = 0
    var correctAnswers = 0
    var incorrectAnswers = 0
    
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val isAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered

    fun getCurrentIsCheated(): Boolean {
        return questionBank[currentIndex].isCheated
    }
    
    fun setTrueCurrentIsCheated() {
        questionBank[currentIndex].isCheated = true
    }

    fun setTrueIsAnswered() {
        questionBank[currentIndex].isAnswered = true
    }
            
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