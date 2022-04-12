package ca.adnl.weatheradnl

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ca.adnl.weatheradnl.models.City

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE City(_id INTEGER NOT NULL UNIQUE, name TEXT NOT NULL UNIQUE, description TEXT, PRIMARY KEY(_id))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS City")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "weather.db"
    }

    fun insertCity(city: City): Long?{
        val db = this.writableDatabase
        println("Inserting ${city.name} ${city.description}")
        val values = ContentValues().apply {
            put("name", city.name)
            put("description", city.description)
        }
        return db?.insert("City", null, values)
    }

    fun updateCity(city: City){
        val db = this.writableDatabase
        val selection = "_id = ?"
        val selectionArgs = arrayOf("${city.id}")
        val values = ContentValues().apply {
            put("name", city.name)
            put("description", city.description)
        }
        db?.update("City", values,selection, selectionArgs)
    }

    fun getAllCities(): List<City>{
        val db = this.readableDatabase
        val cursor = db.query(
            "City",   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val cities = mutableListOf<City>()
        with(cursor) {
            while (moveToNext()) {
                val category = City(id =  getLong(getColumnIndexOrThrow("_id")), name = getString(getColumnIndexOrThrow("name")), description = getString(getColumnIndexOrThrow("description")))
                cities.add(category)
            }
        }
        cursor.close()
        return cities
    }

    fun deleteCity(city: City){
        val db = this.writableDatabase
        val selection = "_id = ?"
        val selectionArgs = arrayOf("${city.id}")
        db?.delete("City",selection, selectionArgs)
    }

}