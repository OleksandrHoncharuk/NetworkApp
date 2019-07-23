package com.example.networkaplication.persistance.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import java.util.Objects

@Entity(tableName = "movie_query")
class MovieQuery {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String? = null
    var date: Long = 0

    constructor() {}

    @Ignore
    constructor(name: String, date: Long) {
        this.name = name
        this.date = date
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MovieQuery?
        return id == that!!.id &&
                date == that.date &&
                name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, date)
    }
}
