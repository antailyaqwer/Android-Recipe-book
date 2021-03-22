package org.antailyaqwer.recipebook

import android.app.Application
import org.antailyaqwer.recipebook.database.Repository

class RecipeBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }
}