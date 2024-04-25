package com.example.mob_dev_portfolio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mob_dev_portfolio.dao.RecipeDao

@TypeConverters(Converters::class)
@Database(entities = [ FavouriteRecipe::class], version = 3)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `favourite_recipes_new` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`title` TEXT NOT NULL" +
                            ")"
                )


                val cursor = database.query("SELECT name FROM sqlite_master WHERE type='table' AND name='favourite_recipes'")
                if (cursor != null && cursor.moveToFirst()) {

                    database.execSQL(
                        "INSERT INTO favourite_recipes_new (id, title) " +
                                "SELECT id, title FROM favourite_recipes"
                    )


                    database.execSQL("DROP TABLE favourite_recipes")
                }
                cursor?.close()


                database.execSQL("ALTER TABLE favourite_recipes_new RENAME TO favourite_recipes")
            }
        }
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    "ALTER TABLE liked_recipes " +
                            "ADD COLUMN `isLiked` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}