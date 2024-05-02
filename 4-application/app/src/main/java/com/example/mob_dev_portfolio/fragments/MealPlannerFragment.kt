package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.MealPlanResponse
import com.example.mob_dev_portfolio.data.RecipeAPI
import com.example.mob_dev_portfolio.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealPlannerFragment: Fragment() {
    private lateinit var recipeApi: RecipeAPI



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.meal_planner_fragment, container, false)


        recipeApi = RetrofitClient.getRecipeAPI()


        val generateButton = view.findViewById<Button>(R.id.generateButton)


        generateButton.setOnClickListener {
            generateMealPlan()
        }


        setUpDescriptionIcons(view)

        return view
    }
    private fun generateMealPlan() {
        val timeFrameEditText = view?.findViewById<EditText>(R.id.timeFrameEditText)
        val targetCaloriesEditText = view?.findViewById<EditText>(R.id.targetCaloriesEditText)
        val dietEditText = view?.findViewById<EditText>(R.id.dietEditText)
        val excludeEditText = view?.findViewById<EditText>(R.id.excludeEditText)


        val timeFrame = timeFrameEditText?.text.toString()
        val targetCalories = targetCaloriesEditText?.text.toString().toIntOrNull() ?: 0
        val diet = dietEditText?.text.toString()
        val exclude = excludeEditText?.text.toString()


        val call = recipeApi.generateMealPlan(timeFrame, targetCalories, diet, exclude, "b94a351e14e94600bc8f1a9cae0008eb")


        call.enqueue(object : Callback<MealPlanResponse> {
            override fun onResponse(call: Call<MealPlanResponse>, response: Response<MealPlanResponse>) {
                if (response.isSuccessful) {
                    val mealPlan = response.body()

                    updateUIWithMealPlan(mealPlan)
                } else {

                    Log.e("MealFragment", "Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MealPlanResponse>, t: Throwable) {
                // Handle failure
                Log.e("MealFragment", "Failed to fetch meal plan", t)
            }
        })
    }

    private fun updateUIWithMealPlan(mealPlan: MealPlanResponse?) {

        val mealPlanTextView = view?.findViewById<TextView>(R.id.mealPlanTextView)


        mealPlanTextView?.text = ""


        mealPlan?.let { plan ->
            mealPlanTextView?.append("Meal Plan:\n\n")

            plan.meals.forEachIndexed { index, meal ->
                mealPlanTextView?.append("${index + 1}. ${meal.title}\n")
                mealPlanTextView?.append("   Ready in minutes: ${meal.readyInMinutes}\n")
                mealPlanTextView?.append("   Servings: ${meal.servings}\n")
                mealPlanTextView?.append("   Source URL: ${meal.sourceUrl}\n\n")
            }


            mealPlanTextView?.append("Nutrient Information:\n")
            mealPlanTextView?.append("Calories: ${plan.nutrients.calories}\n")
            mealPlanTextView?.append("Carbohydrates: ${plan.nutrients.carbohydrates}\n")
            mealPlanTextView?.append("Fat: ${plan.nutrients.fat}\n")
            mealPlanTextView?.append("Protein: ${plan.nutrients.protein}\n")
        }
    }
    private fun setUpDescriptionIcons(view: View) {
        val button = view.findViewById<ImageButton>(R.id.questionMarkButton)

        button.setOnClickListener {

            val popupWindow = PopupWindow(
                LayoutInflater.from(requireContext()).inflate(R.layout.popup_description, null),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )


            popupWindow.showAsDropDown(button)
        }
    }




}

