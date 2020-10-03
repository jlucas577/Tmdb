package org.themoviedb.joaomartins.models

data class Movies(
    val poster_path: String = " ",
    val id: Int = 0,
    val adult: Boolean = false,
    val backdrop_path: String = " ",
    val title: String = " ",
    val overview: String = " "
)