package org.themoviedb.joaomartins.ui.fragments.nowPlaying

import android.os.Bundle
import android.util.Log.d
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
import org.themoviedb.joaomartins.config.Tmdb
import org.themoviedb.joaomartins.converts.DataConverterFactory
import org.themoviedb.joaomartins.models.Movies
import org.themoviedb.joaomartins.requests.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NowPlayingFragment : Fragment() {

    /*
        Widgets
    */
    private lateinit var root: View

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.layoutLoader)
    lateinit var loader: RelativeLayout

    @BindView(R.id.layoutError)
    lateinit var error: RelativeLayout


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

        root = inflater.inflate(R.layout.fragment_now_playing, container, false)

        ButterKnife.bind(this, root)

        retrofit = Retrofit.Builder()
            .baseUrl(Tmdb.tmdbBase)
            .addConverterFactory(DataConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)

        api.run {
            getNowPlaying().enqueue(object : Callback<List<Movies>> {

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


        return root
    }


    /*
        Funções
    */
    private fun loadData(movies: List<Movies>) {

        if (::recyclerView.isInitialized) {

            recyclerView.visibility = View.VISIBLE
            loader.visibility = View.GONE
            error.visibility = View.GONE

            gridLayoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)

            recyclerView.apply {
                layoutManager = gridLayoutManager
                adapter = MoviesAdapters(movies)
            }

        }

    }

    private fun loadError() {

        recyclerView.visibility = View.GONE
        loader.visibility = View.GONE
        error.visibility = View.VISIBLE

    }

}