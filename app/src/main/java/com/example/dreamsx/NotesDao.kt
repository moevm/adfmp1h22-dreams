package com.example.dreamsx

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insert(note: Note)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(note: Note)

    @Delete
     suspend fun delete(note: Note)

    @Query("Select * from notesTable order by id ASC")
     fun getAllNotes(): LiveData<List<Note>>
}

