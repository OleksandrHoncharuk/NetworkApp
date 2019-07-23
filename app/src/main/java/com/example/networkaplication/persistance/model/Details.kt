package com.example.networkaplication.persistance.model


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "details")
class Details {

    @PrimaryKey
    lateinit var id: String
    var title: String? = null
    var details: String? = null
    var genre: String? = null
    var plotSummary: String? = null

    constructor() {}

    @Ignore
    constructor(id: String) {
        this.id = id
    }

    @Ignore
    constructor(id: String, title: String, details: String, genre: String, plotSummary: String) {
        this.id = id
        this.title = title
        this.details = details
        this.genre = genre
        this.plotSummary = plotSummary
    }
}
