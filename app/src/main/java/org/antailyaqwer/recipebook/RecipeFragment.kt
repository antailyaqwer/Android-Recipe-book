package org.antailyaqwer.recipebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import org.antailyaqwer.recipebook.database.RecipeEntity
import java.util.*

class RecipeFragment : Fragment() {

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
        imageView = view.findViewById(R.id.recipe_image_fragment) as ImageView
        nameTextView = view.findViewById(R.id.recipe_name_fragment) as TextView
        dateTextView = view.findViewById(R.id.recipe_date_fragment) as TextView
        difficultyTextView = view.findViewById(R.id.recipe_difficulty_fragment) as TextView
        descriptionTextView = view.findViewById(R.id.recipe_description_fragment) as TextView
        instructionsTextView = view.findViewById(R.id.recipe_instructions_fragment) as TextView

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
        Picasso.get()
            .load(recipe.images[0])
            .into(imageView)
        nameTextView.text = recipe.name
        dateTextView.text = "Last Updated: ${recipe.lastUpdated.toString()}"
        difficultyTextView.text = "Difficulty: ${recipe.difficulty.toString()}"
        descriptionTextView.text = recipe.description
        //TODO Изменить разбиение по строкам.
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
