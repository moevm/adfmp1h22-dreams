package com.example.dreamsx

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


enum class DreamMood {
    COOL, MIDDLE, BAD
}

@Entity(tableName = "notesTable")
class Note (
    @ColumnInfo(name ="title")val notesTitle:String,
    @ColumnInfo(name = "description")val noteDescription:String,
    @ColumnInfo(name="timeStamp")val timeStamp:String,
    @ColumnInfo(name="tag")val tags:String,
    @ColumnInfo(name="moodType")val mood:DreamMood
    )
{
    @PrimaryKey(autoGenerate = true)
    var id = 0
}