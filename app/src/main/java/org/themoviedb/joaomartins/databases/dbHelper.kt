package org.themoviedb.joaomartins.databases

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.themoviedb.joaomartins.models.Movies

class DbHelper (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {

        //Informações sobre o banco de dados
        private val DB_VERSION = 2
        private val DB_NAME = "DB_FAVORITES.db"

        //Informaçoões sobre a tabela
        private val TB_NAME = "favorites"

        //Informaçoões sobre as colunas
        private val CL_ID = "id"
        private val CL_MOVIE = "movie"
        private val CL_POSTER_PATH = "poster_path"
        private val CL_ADULT = "adult"
        private val CL_TITLE = "title"
        private val CL_BACKDROP_PATH = "backdrop_path"
        private val CL_OVERVIEW = "overview"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val tableCreate : String = (
                    "CREATE TABLE $TB_NAME ($CL_ID INTEGER PRIMARY KEY, $CL_MOVIE TEXT, $CL_POSTER_PATH TEXT, $CL_ADULT TEXT, $CL_TITLE TEXT, $CL_BACKDROP_PATH TEXT, $CL_OVERVIEW TEXT)"
                )

        db!!.execSQL(tableCreate)

    }

    override fun onUpgrade(db: SQLiteDatabase?, versionOld: Int, versionNew: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS $TB_NAME")
        onCreate(db)

    }

    val getMovies:List<Movies>
    get() {

        val listMovies = ArrayList<Movies>()
        val selectedQuery = "SELECT * FROM $TB_NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(
            selectedQuery
            , null
        )

        if (cursor.moveToFirst()) {

            do {

                val movie = Movies(
                    id = cursor.getInt(cursor.getColumnIndex(CL_MOVIE))
                    , poster_path = cursor.getString(cursor.getColumnIndex(CL_POSTER_PATH))
                    , adult = cursor.getString(cursor.getColumnIndex(CL_POSTER_PATH))!!.toBoolean()
                    , title = cursor.getString(cursor.getColumnIndex(CL_TITLE))
                    , backdrop_path = cursor.getString(cursor.getColumnIndex(CL_BACKDROP_PATH))
                    , overview = cursor.getString(cursor.getColumnIndex(CL_OVERVIEW))
                )

                listMovies.add(
                    movie
                )

            } while (cursor.moveToNext())

        }

        return listMovies

    }

    fun getMovie(int: Int): Int {

        val db = this.readableDatabase
        val numRows = DatabaseUtils.longForQuery(
            db,
            "SELECT COUNT(*) FROM $TB_NAME WHERE $CL_MOVIE = $int",
            null
        ).toInt()

        return numRows

    }

    fun newMovies(movie : Movies) {

        val db = this.writableDatabase
        val value = ContentValues()
            value.put(CL_MOVIE, movie.id)
            value.put(CL_POSTER_PATH, movie.poster_path)
            value.put(CL_ADULT, movie.adult)
            value.put(CL_TITLE, movie.title)
            value.put(CL_BACKDROP_PATH, movie.backdrop_path)
            value.put(CL_OVERVIEW, movie.overview)

        db.insert(
            TB_NAME
            , null
            , value
        )
        db.close()

    }

    fun deleteMovies(movie : Movies) {

        val db = this.writableDatabase

        db.delete(
            TB_NAME
            , "$CL_MOVIE = ?"
            , arrayOf(movie.id.toString())
        )
        db.close()

    }

}