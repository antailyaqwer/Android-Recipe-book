package org.antailyaqwer.recipebook

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.antailyaqwer.recipebook.database.RecipeEntity
import java.util.*

class RecipeListFragment private constructor() : Fragment() {

    interface Callbacks {
        fun onRecipeSelected(recipeID: UUID)
    }

    private var callbacks: Callbacks? = null

    private val listViewModel: RecipeListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipe_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.recipe_recycle_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = RecipeListAdapter()
//        listViewModel.tempFun()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.recipeListLiveData.observe(
            viewLifecycleOwner, { updateUI(it) }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(recipes: List<RecipeEntity>) {
        (recyclerView.adapter as RecipeListAdapter).submitList(recipes)
    }

    companion object {
        fun newInstance() = RecipeListFragment()
    }

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var recipe: RecipeEntity
        private val recipeImageView: ImageView =
            itemView.findViewById(R.id.recipe_image_list)
        private val nameTextView: TextView = itemView.findViewById(R.id.recipe_name_list)
        private val descriptionTextView: TextView =
            itemView.findViewById(R.id.recipe_description_list)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(recipe: RecipeEntity) {
            this.recipe = recipe
            //TODO Изменить строку на картинку
//            recipeImageView = ""
            nameTextView.text = recipe.name
            descriptionTextView.text = recipe.name.takeWhile {
                var temp: Byte = 0
                if (it == '\n') temp++
                temp < 2
            }

        }

        override fun onClick(v: View?) {
            callbacks?.onRecipeSelected(recipe.id)
        }
    }

    inner class RecipeDiffCallBack : DiffUtil.ItemCallback<RecipeEntity>() {

        override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean =
            oldItem == newItem
    }

    inner class RecipeListAdapter :
        ListAdapter<RecipeEntity, RecipeViewHolder>(RecipeDiffCallBack()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder =
            RecipeViewHolder(
                layoutInflater.inflate(R.layout.recipe_list_item, parent, false)
            )

        override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }
}