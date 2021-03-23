package org.antailyaqwer.recipebook

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.squareup.picasso.Picasso
import org.antailyaqwer.recipebook.database.RecipeEntity
import java.util.*

class RecipeListFragment : Fragment() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipe_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.recipe_recycle_view) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = RecipeListAdapter()
        listViewModel.parseObjects()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.recipeListLiveData.observe(
            viewLifecycleOwner, { recipes -> updateUI(recipes) }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.recipe_list_item, menu)
        val searchView = menu.findItem(R.id.search_bar_recipe).actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("RecipeListFragment", "text: $newText")
                    if (newText == null || newText.isEmpty()) {
                        listViewModel.getAllRecipesByNameAscending().observe(viewLifecycleOwner) {
                            updateUI(it)
                        }
                    } else {
                        listViewModel.searchByNameOrDescriptionOrInstructions(newText)
                            .observe(viewLifecycleOwner) {
                                updateUI(it)
                            }
                    }
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_name_ascending -> {
                listViewModel.getAllRecipesByNameAscending().observe(viewLifecycleOwner) {
                    recyclerView.adapter = RecipeListAdapter()
                    updateUI(it)
                }
                true
            }
            R.id.sort_by_name_descending -> {
                listViewModel.getAllRecipesByNameDescending().observe(viewLifecycleOwner) {
                    recyclerView.adapter = RecipeListAdapter()
                    updateUI(it)
                }
                true
            }
            else -> false
        }
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
        private var recipeImageView: ImageView =
            itemView.findViewById(R.id.recipe_image_list) as ImageView
        private val nameTextView: TextView =
            itemView.findViewById(R.id.recipe_name_list) as TextView
        private val descriptionTextView: TextView =
            itemView.findViewById(R.id.recipe_description_list) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(recipe: RecipeEntity) {
            this.recipe = recipe
            Picasso.get()
                .load(recipe.images[0])
                .into(recipeImageView)

            nameTextView.text = recipe.name
            if (recipe.description != null) {
                val regex = """^.*?[.!?](?:\s|$)""".toRegex()
                descriptionTextView.text =
                    regex.find(recipe.description)?.value ?: ""
            }

        }

        override fun onClick(v: View?) {
            callbacks?.onRecipeSelected(recipe.uuid)
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