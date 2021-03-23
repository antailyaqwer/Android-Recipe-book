package org.antailyaqwer.recipebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import org.antailyaqwer.recipebook.database.RecipeEntity
import java.util.*
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import kotlin.math.abs

private const val TAG = "RecipeFragment"

class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var recipe: RecipeEntity
    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var difficultyTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recipeId: UUID = arguments?.getSerializable("id") as UUID
        viewModel.loadRecipe(recipeId)
        setHasOptionsMenu(false)
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
        gestureDetector = GestureDetector(context, GestureListener())
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
        updateImage()
        nameTextView.text = recipe.name
        dateTextView.text = try {
            val temp = Date(recipe.lastUpdated.toLong())
            DateFormat.format("dd/MM/yyyy HH:mm", temp)
        } catch (e: Exception) {
            Log.d("RecipeFragment", "Can't Parse Date", e as Throwable)
            "Can't parse date"
        }
        difficultyTextView.text = "Difficulty: ${recipe.difficulty.toString()}"
        descriptionTextView.text = recipe.description
        instructionsTextView.text = with(java.lang.StringBuilder())
        {
            this.append("Instructions:\n")
            var temp: String = recipe.instructions ?: ""
            do {
                this.append(temp.substringBefore("<br>"), '\n')
                temp = temp.substringAfter("<br>")
            } while (temp.contains("<br>", true))
            this.toString()
        }
    }

    private fun updateImage() {
        Picasso.get()
            .load(recipe.images[0])
            .into(imageView)
        imageView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                Log.d(TAG, "setOnTouchListener")
                gestureDetector.onTouchEvent(event)
            }
            true
        }

    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val thresholdVelocity = 50
//        override fun onFling(
//            e1: MotionEvent?,
//            e2: MotionEvent?,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            //Если свайп недостаточно быстрый
//            if (abs(velocityX) < thresholdVelocity && abs(velocityY) < thresholdVelocity) return false
//            if (abs(velocityX) > abs(velocityY)) {
//                //Горизонтальный жест вправо
//                if (velocityX >= 0) {
//                    Log.d(TAG, "Horizontal swipe to right")
//                } else {//Горизонтальный жест влево
//                    Log.d(TAG, "Horizontal swipe to left")
//                }
//            } else return false //Игнорируем ось Y
//            return true
//        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            //Если свайп недостаточно быстрый
            if (abs(distanceX) < thresholdVelocity && abs(distanceY) < thresholdVelocity) return false
            if (abs(distanceX) > abs(distanceY)) {
                //Горизонтальный жест вправо
                if (distanceX >= 0) {
                    Log.d(TAG, "Horizontal swipe to right")
                } else {//Горизонтальный жест влево
                    Log.d(TAG, "Horizontal swipe to left")
                }
            } else return false //Игнорируем ось Y
            return true
        }
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
