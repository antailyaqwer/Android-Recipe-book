package org.antailyaqwer.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity(),
    RecipeListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = RecipeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    /**
     * При нажатии на элемент списка в RecipeListFragment
     * выбирается экземпляр RecipeFragment с recipeID
     */
    override fun onRecipeSelected(recipeID: UUID) {
        val fragment = RecipeFragment.newInstance(recipeID)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}