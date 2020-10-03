package org.themoviedb.joaomartins

import android.os.Bundle
import android.view.Menu
import android.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import butterknife.BindView
import butterknife.BindColor
import butterknife.ButterKnife

class MainActivity : AppCompatActivity() {

    /*
        Widgets
    */
    @BindView(R.id.nav_view)
    lateinit var pageNavigation: BottomNavigationView


    /*
        Layout
    */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        val navController = findNavController(R.id.nav_host_fragment)

        pageNavigation.setupWithNavController(navController)

    }

}
