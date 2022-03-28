package com.example.dreamsx

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.security.AccessControlContext

@Database(entities = arrayOf(Note::class), version = 1) //, exportSchema = false
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getNotesDao(): NotesDao

    companion object{
//        val migration1_2 = object : Migration(1, 2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE notesTable ADD COLUMN isActive INTEGER NOT NULL DEFAULT(1)")
//            }
//        }

        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatbase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    //.addMigrations(migration1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}

// Database class after the version update.
//@Database(
//    version = 2,
//    entities = [Note::class],
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ]
//)

//abstract class NoteDatabase2 : RoomDatabase() {
//    abstract fun getNotesDao(): NotesDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: NoteDatabase? = null
//
//        fun getDatbase(context: Context): NoteDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    NoteDatabase::class.java,
//                    "note_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}