package com.example.historyassignment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuestionsActivity : AppCompatActivity() {

    private var score = 0
    private var questionIndex = 0
    private lateinit var countDownTimer: CountDownTimer

    private val timePerQuestionMillis = 30_000L

    private val questions = listOf(
        Triple("Who was the first emperor of China?",
            listOf("Qin Shi Huang", "Genghis Khan", "Kublai Khan", "Liu Bang"),
            "Qin Shi Huang"),
        Triple("What year did the American Civil War begin?",
            listOf("1776", "1861", "1914", "1941"),
            "1861"),
        Triple("Which empire was ruled by Alexander the Great?",
            listOf("Roman Empire", "Macedonian Empire", "Persian Empire", "Ottoman Empire"),
            "Macedonian Empire"),
        Triple("Which battle marked the end of the Napoleonic Wars?",
            listOf("Battle of Waterloo", "Battle of Leipzig", "Battle of Trafalgar", "Battle of Austerlitz"),
            "Battle of Waterloo"),
        Triple("Who was the leader of the Soviet Union during World War II?",
            listOf("Vladimir Lenin", "Joseph Stalin", "Nikita Khrushchev", "Mikhail Gorbachev"),
            "Joseph Stalin")
    )

    private lateinit var questionText: TextView
    private lateinit var optionA: Button
    private lateinit var optionB: Button
    private lateinit var optionC: Button
    private lateinit var optionD: Button
    private lateinit var nextButton: Button
    private lateinit var timerText: TextView

    private var selectedAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        questionText = findViewById(R.id.questionText)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        nextButton = findViewById(R.id.nextButton)
        timerText = findViewById(R.id.timerText)

        // Set up answer buttons
        listOf(optionA, optionB, optionC, optionD).forEach { btn ->
            btn.setOnClickListener {
                countDownTimer.cancel()
                selectedAnswer = btn.text.toString()
                nextButton.isEnabled = true
                btn.setBackgroundColor(0xFFAAF0D1.toInt()) // highlight selected
            }
        }

        // Next button
        nextButton.setOnClickListener {
            countDownTimer.cancel()
            checkAnswerAndProceed()
        }

        // Load first question
        loadQuestion()
    }

    private fun loadQuestion() {
        // Reset UI
        listOf(optionA, optionB, optionC, optionD).forEach {
            it.setBackgroundColor(0xFFDDDDDD.toInt())
        }
        nextButton.isEnabled = false
        selectedAnswer = null

        // Display question and options
        val (qText, opts, _) = questions[questionIndex]
        questionText.text = qText
        optionA.text = opts[0]
        optionB.text = opts[1]
        optionC.text = opts[2]
        optionD.text = opts[3]

        // Start timer for this question
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timePerQuestionMillis, 1_000) {
            override fun onTick(ms: Long) {
                timerText.text = "Time left: ${ms / 1000}s"
            }
            override fun onFinish() {
                timerText.text = "Time's up!"
                checkAnswerAndProceed()
            }
        }.start()
    }

    private fun checkAnswerAndProceed() {
        // Score it
        if (selectedAnswer == questions[questionIndex].third) {
            score++
        }

        // Move to next
        questionIndex++
        if (questionIndex < questions.size) {
            loadQuestion()
        } else {
            // Quiz over
            Intent(this, ScoreActivity::class.java).also {
                it.putExtra("FINAL_SCORE", score)
                it.putExtra("TOTAL_QUESTIONS", questions.size)
                startActivity(it)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}
