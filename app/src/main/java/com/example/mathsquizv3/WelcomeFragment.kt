package com.example.mathsquizv3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import androidx.navigation.findNavController


class Welcome : Fragment() {

    var multiply: Int = 0
    var divide: Int = 0
    var subtract: Int = 0
    var addition: Int = 0
    var timePerQuestion: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        val startButton = view.findViewById<Button>(R.id.start)
        // these switches are a little out-dated. there are newer alternatives
        val multSwitch = view.findViewById<Switch>(R.id.multiplySwitch)
        val addSwitch = view.findViewById<Switch>(R.id.addSwitch)
        val divSwitch = view.findViewById<Switch>(R.id.divideSwitch)
        val subtractSwitch = view.findViewById<Switch>(R.id.subtractSwitch)


        // these check for if each switch is on or off, if they are then it sets a flag to one, if not, they get set to 0
        multSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) "multSwitch:ON" else "multSwitch:OFF"
            multiply = 1 - multiply
        }
        addSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) "addSwitch:ON" else "addSwitch:OFF"
            addition = 1 - addition
        }
        divSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) "multSwitch:ON" else "multSwitch:OFF"
            divide = 1 - divide
        }
        subtractSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) "addSwitch:ON" else "addSwitch:OFF"
            subtract = 1 - subtract
        }

        timePerQuestion = 60

        startButton.setOnClickListener {
            // TODO - Add validation for options //
            if(multiply == 1 || addition == 1 || divide == 1 || subtract == 1) {
                val level = view.findViewById<Spinner>(R.id.levelSelect).selectedItem
                timePerQuestion = getGameMode(level.toString())

                val action = WelcomeDirections
                    .actionWelcomeToGameFragment(
                        multiply,
                        divide,
                        addition,
                        subtract,
                        timePerQuestion
                    ) // this passes the 4 flag values to the next fragment
                view.findNavController()
                    .navigate(action)
            }
        }
        return view
    }

    fun getGameMode(level: String): Long {
        return when(level) {
            "Easy" -> 0
            "Medium" -> 10
            "Hard" -> 5
            else -> 120
        }
    }

}