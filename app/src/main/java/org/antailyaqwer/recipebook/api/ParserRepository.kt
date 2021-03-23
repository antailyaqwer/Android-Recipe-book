package org.antailyaqwer.recipebook.api

import android.util.Log
import com.google.gson.GsonBuilder
import org.antailyaqwer.recipebook.database.RecipeEntity
import org.antailyaqwer.recipebook.database.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.concurrent.Executors

private const val TAG = "ParserRepository"

class ParserRepository {

    private val instanceApi: ParserApi
    private val executor = Executors.newSingleThreadExecutor()

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://test.kode-t.ru/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .create()
                )
            )
            .build()

        instanceApi = retrofit.create(ParserApi::class.java)
    }

    fun getRecipes(): List<RecipeEntity> {
        val mutableList: MutableList<RecipeEntity> = ArrayList()
        val request: Call<RecipeResponse> = instanceApi.getRecipes()

        request.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                Log.d(TAG, "Parsing json...")
                val recipeResponse: RecipeResponse? = response.body()
                val items: List<RecipeEntity> = recipeResponse?.recipes ?: mutableListOf()
                val repository = Repository.get()
                executor.execute {
                    items.forEach {
                        if (it.uuid.toString().isNotEmpty() &&
                            !repository.hasRecipe(it.uuid)
                        ) {
                            repository.addRecipe(it)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.d(TAG, "Can't parse json", t)
            }
        })
        return mutableList
    }
}