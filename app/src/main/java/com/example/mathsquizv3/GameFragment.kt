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

    // Initialising the fragment variables
    var num1: Int = -1
    var num2: Int = -1
    var ans: Int = -1
    var numQs: Int = 0
    var timeLeft: Int = 10 // this will be decremented by the timer
    var score: Int = 0 // this will be incremented for each correct answer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val timePerQuestion = GameFragmentArgs.fromBundle(requireArguments()).timePerQuestion


        val view = inflater.inflate(R.layout.fragment_game, container, false)
        val tvQuestion: TextView = view.findViewById<TextView>(R.id.question) as TextView //This is the text display for the question
        val countdown: TextView = view.findViewById<TextView>(R.id.countdown) as TextView //This is the text display for the timer
        if(timePerQuestion.toInt() == 0){
            countdown.visibility=View.INVISIBLE
        }
        //This is the text box for entering the answer - It could be changed to a TextView, and adding buttons for the user to enter the answer
        val userAns: EditText = view.findViewById<EditText>(R.id.editTextNumber) as EditText

        // this line calls the parseChoices() method. This will process the different question choices, and allow us to only generate questions of a certain type
        val userChoices = parseChoices()
        // This allows us to control the submit button
        val submitButton = view.findViewById<Button>(R.id.submitAns)

        fun newQuestion(timer: CountDownTimer) {
            if (numQs == 10){
                timer.cancel()
                endGame()
            } else {
                numQs += 1
                println(numQs)
                tvQuestion.text = generateQuestion(userChoices)
                if(timePerQuestion.toInt() != 0) {
                    timer.cancel()
                    timeLeft = timePerQuestion.toInt()
                    timer.start()
                }
            }
        }

        // this starts a timer that runs for totalTime seconds - we can use this variable to set different levels\
        var questionTimer = object: CountDownTimer(timePerQuestion*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft -= 1
                println(timeLeft)
                countdown.text = timeLeft.toString()
            }

            override fun onFinish() {
                // when the timer has finished, we move to the next fragment
                newQuestion(this)
            }
        }

        // this block of code tells the app what to do when the submit button is clicked
        submitButton.setOnClickListener {
            if(userAns.text.toString() == ""){
                println("no data entered") // TODO - set up a warning TextView //
            } else {
                score += checkAns(
                    ans,
                    userAns.text.toString().toInt()
                ) // this checks whether the user has entered the correct answer and adds 1 to the score if they have
                userAns.setText("") // This clears the users answer box
                newQuestion(questionTimer)
            }
        }

        newQuestion(questionTimer)

        return view
    }

    private fun endGame() {
            val finalScore = score
            val action = GameFragmentDirections
                .actionGameFragmentToResultFragment(finalScore) // this line allows us to pass the finalScore variable to the next fragment
            view?.findNavController()
                ?.navigate(action)
    }

    private fun parseChoices(): BooleanArray {
        // this works by collecting the values passed to the fragment
        // we then check if any of these values are not 0.
        // if they are, we assign the corresponding variable (i.e. a,b,c,d) to true
        // these are the question types we will be creating for the user
        val newArgs = GameFragmentArgs.fromBundle(requireArguments())
        val a: Boolean = newArgs.multiply != 0
        val b: Boolean = newArgs.addition != 0
        val c: Boolean = newArgs.subtract != 0
        val d: Boolean = newArgs.divide != 0

        return booleanArrayOf(a,b,c,d) // this will look something like this [true, true, false, false]
    }

    // checks for equivalence of 2 numbers, returns either 1 or 0. this is used to add a score.
    private fun checkAns(num1: Int, num2: Int): Int{
        if(num1 == num2) {
            return 1
        }
        return 0
    }

    // this generates a new question based on our array of true/false values
    private fun generateQuestion(questionChoice: BooleanArray): String {
        // first chooses a value for a question type
        var questionTypeChoice: Int = (0..3).random()
        // if this type is not valid, choose another until it is valid
        while (!(questionChoice[questionTypeChoice])) {
            questionTypeChoice = (0..3).random()
        }

        // TODO - update randomness //
        // currently, randomness doesn't work (see dice-roller example to fix this)
        //this construct checks the value of questionTypeChoice and runs different code depending on the value
        when (questionTypeChoice) {
            0 -> {
                // multiply
                num1 = (1..13).random()
                num2 = (1..13).random()
                ans = num1 * num2
                return(num1.toString() + " x " + num2.toString())
            }
            1 -> {
                // addition
                num1 = (1..50).random()
                num2 = (1..50).random()
                ans = num1 + num2
                return(num1.toString() + " + " + num2.toString())
            }
            2 -> {
                // subtraction
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
                // division
                num1 = (1..13).random()
                ans = (1..13).random()
                num2 = num1*ans
                return(num2.toString() + " / " + num1.toString())
            }
        }
        // if something goes wrong, the question will be set to this
        return("Something went wrong..")

    }

}