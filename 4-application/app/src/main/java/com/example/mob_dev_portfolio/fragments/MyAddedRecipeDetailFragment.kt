package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mob_dev_portfolio.R

class MyAddedRecipeDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val ingredients = arguments?.getString("ingredients")
        val instructions = arguments?.getString("instructions")
        val calories = arguments?.getString("calories")


        view.findViewById<TextView>(R.id.mytitleTextView).text = title
        view.findViewById<TextView>(R.id.myingredientsContent).text = ingredients
        view.findViewById<TextView>(R.id.myinstructionsContent).text = instructions
        view.findViewById<TextView>(R.id.mycaloriesContent).text = calories


        view.findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            activity?.onBackPressed()
        }
    }
}

