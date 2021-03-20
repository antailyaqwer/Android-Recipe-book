package org.antailyaqwer.recipebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import org.antailyaqwer.recipebook.database.RecipeEntity
import java.util.*

class RecipeFragment private constructor() : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var recipe: RecipeEntity
    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var difficultyTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var instructionsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recipeId: UUID = arguments?.getSerializable("id") as UUID
        viewModel.loadRecipe(recipeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipe_fragment, container, false)
        imageView = view.findViewById(R.id.recipe_image_list) as ImageView
        nameTextView = view.findViewById(R.id.recipe_name_list) as TextView
        dateTextView = view.findViewById(R.id.recipe_date) as TextView
        difficultyTextView = view.findViewById(R.id.recipe_difficulty) as TextView
        descriptionTextView = view.findViewById(R.id.recipe_description_list) as TextView
        instructionsTextView = view.findViewById(R.id.recipe_instructions) as TextView

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.recipeLiveData.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                this.recipe = recipe
                updateUI()
            }
        }
    }

    private fun updateUI() {
        //TODO изменить ImageView
//        imageView.
        nameTextView.text = recipe.name
        dateTextView.text = recipe.lastUpdated.toString()
        difficultyTextView.text = recipe.difficulty.toString()
        descriptionTextView.text = recipe.description
        instructionsTextView.text = recipe.instructions

    }

    companion object {
        fun newInstance(recipeId: UUID): RecipeFragment =
            RecipeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("id", recipeId)
                }
            }
    }
}
