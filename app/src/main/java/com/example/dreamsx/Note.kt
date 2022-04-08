package com.example.dreamsx

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

enum class DreamMood {
    COOL, MIDDLE, BAD
}


@Entity(tableName = "notesTable")
class Note (
    @ColumnInfo(name ="title")val notesTitle:String,
    @ColumnInfo(name = "description")val noteDescription:String,
    @ColumnInfo(name="timeStamp")val timeStamp:String,
    @ColumnInfo(name="tag")val noteTags:String,
    @ColumnInfo(name="moodType")val mood:DreamMood
    ) : Serializable
{
    @PrimaryKey(autoGenerate = true)
    var id = 0

}