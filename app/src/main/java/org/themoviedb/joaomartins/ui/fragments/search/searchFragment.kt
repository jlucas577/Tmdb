package org.themoviedb.joaomartins.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import org.themoviedb.joaomartins.R
import org.themoviedb.joaomartins.adapters.MoviesAdapters
import org.themoviedb.joaomartins.config.Tmdb
import org.themoviedb.joaomartins.converts.DataConverterFactory
import org.themoviedb.joaomartins.models.Movies
import org.themoviedb.joaomartins.requests.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    /*
        Widgets
    */
    private lateinit var root: View

    @BindView(R.id.layoutIntro)
    lateinit var layoutIntro: RelativeLayout

    @BindView(R.id.layoutLoader)
    lateinit var layoutLoader: RelativeLayout

    @BindView(R.id.layoutSearch)
    lateinit var layoutSearch: RelativeLayout

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.search_button)
    lateinit var buttonSearch: Button

    @BindView(R.id.back_button)
    lateinit var buttonBack: Button

    @BindView(R.id.search_textarea)
    lateinit var textSearch: EditText


    /*
        Classes
    */
    private lateinit var retrofit: Retrofit
    private lateinit var api: ApiService
    private lateinit var gridLayoutManager: GridLayoutManager


    /*
        Layout
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_search, container, false)

        ButterKnife.bind(this, root)


        buttonSearch.setOnClickListener {
            initSearch()
        }

        buttonBack.setOnClickListener {
            textSearch.text.clear()

            layoutIntro.visibility = View.VISIBLE
            layoutLoader.visibility = View.GONE
            layoutSearch.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }


        return root

    }


    /*
        Funções
    */
    private fun initSearch() {

        layoutIntro.visibility = View.GONE
        layoutLoader.visibility = View.VISIBLE
        layoutSearch.visibility = View.GONE
        recyclerView.visibility = View.GONE

        loadSearch()

    }

    private fun loadSearch() {

        val query = textSearch.text

        retrofit = Retrofit.Builder()
            .baseUrl(Tmdb.tmdbBase)
            .addConverterFactory(DataConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)

        api.run {
            getSearch("https://api.themoviedb.org/3/search/movie?api_key=d894a441bacf1509fd4a16155bc31b97&query=$query").enqueue(object : Callback<List<Movies>> {

                override fun onResponse(call: Call<List<Movies>>, response: Response<List<Movies>>) {
                    loadData(
                        response.body()!!
                    )
                }

                override fun onFailure(call: Call<List<Movies>>, t: Throwable) {
                    loadError()
                }

            })
        }

    }

    private fun loadData(movies: List<Movies>) {

        if (::recyclerView.isInitialized && movies.isNotEmpty()) {

            layoutIntro.visibility = View.GONE
            layoutLoader.visibility = View.GONE
            layoutSearch.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            gridLayoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)

            recyclerView.apply {
                layoutManager = gridLayoutManager
                adapter = MoviesAdapters(movies)
            }

        } else {

            loadError()

        }

    }

    private fun loadError() {

        layoutIntro.visibility = View.GONE
        layoutLoader.visibility = View.GONE
        layoutSearch.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

    }

}