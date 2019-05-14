package com.example.ownrepositarypatternsample.data.local

object DatabaseAnnotation {
    // App Database
    const val DATABASE_VERSION = 3
    const val DATABASE_NAME = "TheMovies.db"
    // Movie table
    const val TABLE_MOVIE = "Movie"
    const val ID = "id"
    const val PAGE = "page"
    const val KEYWORDS = "keywords"
    const val VIDEOS = "videos"
    const val REVIEWS = "reviews"
    const val POSTER_PATH = "poster_path"
    const val ADULT = "adult"
    const val OVERVIEW = "overview"
    const val RELEASE_DATE = "release_date"
    const val GENRE_IDS = "genre_ids"
    const val ORIGINAL_TITLE = "original_title"
    const val ORIGINAL_LANGUAGE = "original_language"
    const val TITLE = "title"
    const val BACKDROP_PATH = "backdrop_path"
    const val POPULARITY = "popularity"
    const val VOTE_COUNT = "vote_count"
    const val VIDEO = "video"
    const val VOTE_AVERAGE = "vote_average"
    // Movie table
    const val TABLE_PEOPLE = "People"
    const val PROFILE_PATH = "profile_path"
    const val NAME = "name"
    // Tv table
    const val TABLE_TV = "Tv"
    const val FIRST_AIR_DATE = "first_air_date"
    const val ORIGIN_COUNTRY = "origin_country"
    const val ORIGINAL_NAME = "original_name"
}