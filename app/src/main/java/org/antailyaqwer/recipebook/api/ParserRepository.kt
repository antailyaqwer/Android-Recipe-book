package org.antailyaqwer.recipebook.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import org.antailyaqwer.recipebook.database.RecipeEntity
import org.antailyaqwer.recipebook.database.RecipeTypeConverter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.util.*

private const val TAG = "ParserRepository"

class ParserRepository {

    private val instanceApi: ParserApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://test.kode-t.ru/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
//                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//                        .setDateFormat(DateFormat.FULL, DateFormat.FULL)
//                        .excludeFieldsWithoutExposeAnnotation()
                        .create()
                )
            )
            .build()

        instanceApi = retrofit.create(ParserApi::class.java)
    }

    fun getRecipes(): LiveData<List<RecipeEntity>> {
        val responseLiveData: MutableLiveData<List<RecipeEntity>> = MutableLiveData()
        val request: Call<RecipeResponse> = instanceApi.getRecipes()

        request.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                Log.d(TAG, "Parsing json...")
                val recipeResponse: RecipeResponse? = response.body()
                Log.d(TAG, recipeResponse.toString())
                val items: List<RecipeEntity> = recipeResponse?.recipes ?: mutableListOf()
//                items = items.filterNot { it.name.isBlank() }
                responseLiveData.value = items
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.d(TAG, "Can't parse json", t)
            }
        })
        return responseLiveData
    }
}