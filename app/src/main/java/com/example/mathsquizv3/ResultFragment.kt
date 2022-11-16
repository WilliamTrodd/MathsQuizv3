package com.example.mathsquizv3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO - back stack adjustments //
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        val score = ResultFragmentArgs.fromBundle(requireArguments()).score
        val scoreView: TextView = view.findViewById<TextView>(R.id.resultDisplay) as TextView
        val homeButton = view.findViewById<Button>(R.id.homeButton) as Button
        scoreView.text = score.toString()

        homeButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_resultFragment_to_welcome)
        }

        return view
    }
}