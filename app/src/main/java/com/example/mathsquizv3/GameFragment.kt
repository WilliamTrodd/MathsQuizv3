package com.example.mathsquizv3

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.findNavController


class GameFragment : Fragment() {

    var num1: Int = -1
    var num2: Int = -1
    var ans: Int = -1
    var totalTime: Long = 10
    var timeLeft: Int = 10
    var score: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        val tvQuestion: TextView = view.findViewById<TextView>(R.id.question) as TextView
        val countdown: TextView = view.findViewById<TextView>(R.id.countdown) as TextView
        val userAns: EditText = view.findViewById<EditText>(R.id.editTextNumber) as EditText
        //val testUserChoice: ArrayList<Boolean> = savedInstanceState.uChoices
        val userChoices = parseChoices()
        val submitButton = view.findViewById<Button>(R.id.submitAns)

        submitButton.setOnClickListener {
            score += checkAns(ans, userAns.text.toString().toInt())
            println(score.toString())
            tvQuestion.text = generateQuestion(userChoices)
            userAns.setText(ans.toString())
        }

        object: CountDownTimer(totalTime*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft -= 1
                countdown.text = timeLeft.toString()
            }

            override fun onFinish() {
                val finalScore = score
                val action = GameFragmentDirections
                                .actionGameFragmentToResultFragment(finalScore)
                view.findNavController()
                    .navigate(action)
            }
        }.start()

        tvQuestion.text = generateQuestion(userChoices)

        return view
    }

    private fun parseChoices(): BooleanArray {
        val newArgs = GameFragmentArgs.fromBundle(requireArguments())
        val a: Boolean = newArgs.multiply != 0
        val b: Boolean = newArgs.addition != 0
        val c: Boolean = newArgs.subtract != 0
        val d: Boolean = newArgs.divide != 0

        return booleanArrayOf(a,b,c,d)
    }


    private fun checkAns(num1: Int, num2: Int): Int{
        if(num1 == num2) {
            return 1
        }
        return 0
    }

    private fun generateQuestion(questionChoice: BooleanArray): String {
        var questionTypeChoice: Int = (0..3).random()

        while (!(questionChoice[questionTypeChoice])) {
            questionTypeChoice = (0..3).random()
            println(questionTypeChoice)
        }

        when (questionTypeChoice) {
            0 -> {
                num1 = (1..13).random()
                num2 = (1..13).random()
                ans = num1 * num2
                return(num1.toString() + " x " + num2.toString())
            }
            1 -> {
                num1 = (1..50).random()
                num2 = (1..50).random()
                ans = num1 + num2
                return(num1.toString() + " + " + num2.toString())
            }
            2 -> {
                num1 = (1..50).random()
                num2 = (1..50).random()
                var temp: Int = 0
                if (num1<num2){
                    temp = num1
                    num1 = num2
                    num2 = temp
                }
                ans = num1-num2
                return(num1.toString() + " - " + num2.toString())
            }
            3 -> {
                num1 = (1..13).random()
                ans = (1..13).random()
                num2 = num1*ans
                return(num2.toString() + " / " + num1.toString())
            }
        }

        return("Something went wrong..")

    }

}