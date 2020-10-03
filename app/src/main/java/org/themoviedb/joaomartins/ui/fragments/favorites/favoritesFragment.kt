package org.themoviedb.joaomartins.ui.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import org.themoviedb.joaomartins.R
import org.themoviedb.joaomartins.adapters.MoviesAdapters
import org.themoviedb.joaomartins.databases.DbHelper
import org.themoviedb.joaomartins.models.Movies

class FavoritesFragment : Fragment() {

    /*
        Widgets
    */
    private lateinit var root: View

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.layoutLoader)
    lateinit var loader: RelativeLayout

    @BindView(R.id.layoutEmpty)
    lateinit var empty: RelativeLayout


    /*
        Classes
    */
    private lateinit var gridLayoutManager: GridLayoutManager


    /*
        Banco de dados
    */
    lateinit var db:DbHelper
    var listMovies:List<Movies> = ArrayList<Movies>()


    /*
       Layout
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_favorites, container, false)

        ButterKnife.bind(this, root)

        db = DbHelper(this.context!!)

        getFavorites()

        return root

    }


    /*
        Funções
    */
    private fun getFavorites() {

        listMovies = db.getMovies

        if (listMovies.isNotEmpty()) {

            if (::recyclerView.isInitialized) {

                recyclerView.visibility = View.VISIBLE
                loader.visibility = View.GONE
                empty.visibility = View.GONE

                gridLayoutManager =
                    GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)

                recyclerView.apply {
                    layoutManager = gridLayoutManager
                    adapter = MoviesAdapters(listMovies)
                }

            } else {

                showEmpty()

            }

        } else {

            showEmpty()

        }

    }

    private fun showEmpty() {

        recyclerView.visibility = View.GONE
        loader.visibility = View.GONE
        empty.visibility = View.VISIBLE

    }


    /*
       Ciclo de vida
    */
    override fun onResume() {
        super.onResume()

        getFavorites()
    }


}