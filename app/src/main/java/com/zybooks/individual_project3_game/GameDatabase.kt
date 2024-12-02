package com.zybooks.individual_project3_game

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date

class GameDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GameProgress.db"
        private const val DATABASE_VERSION = 1

        // Tables
        private const val TABLE_PARENTS = "parents"
        private const val TABLE_KIDS = "kids"
        private const val TABLE_GAME_PROGRESS = "game_progress"

        // Common Columns
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password" // In production, use proper password hashing

        // Parent Table Columns
        private const val COLUMN_EMAIL = "email"

        // Kid Table Columns
        private const val COLUMN_PARENT_ID = "parent_id"
        private const val COLUMN_AGE = "age"

        // Progress Table Columns
        private const val COLUMN_KID_ID = "kid_id"
        private const val COLUMN_LEVEL = "level"
        private const val COLUMN_SCORE = "score"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Parents table
        val createParentsTable = """
            CREATE TABLE $TABLE_PARENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL
            )
        """.trimIndent()

        // Create Kids table
        val createKidsTable = """
            CREATE TABLE $TABLE_KIDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_AGE INTEGER NOT NULL,
                $COLUMN_PARENT_ID INTEGER,
                FOREIGN KEY($COLUMN_PARENT_ID) REFERENCES $TABLE_PARENTS($COLUMN_ID)
            )
        """.trimIndent()

        // Create Progress table
        val createProgressTable = """
            CREATE TABLE $TABLE_GAME_PROGRESS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_KID_ID INTEGER,
                $COLUMN_LEVEL INTEGER NOT NULL,
                $COLUMN_SCORE INTEGER NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                FOREIGN KEY($COLUMN_KID_ID) REFERENCES $TABLE_KIDS($COLUMN_ID)
            )
        """.trimIndent()

        db.execSQL(createParentsTable)
        db.execSQL(createKidsTable)
        db.execSQL(createProgressTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades here
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAME_PROGRESS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_KIDS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARENTS")
        onCreate(db)
    }

    // Parent Account Management
    fun createParentAccount(username: String, password: String, email: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password) // In production, hash the password
            put(COLUMN_EMAIL, email)
        }
        return db.insert(TABLE_PARENTS, null, values)
    }

    fun getParentByUsername(username: String): Parent? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PARENTS,
            null,
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            Parent(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            )
        } else null.also { cursor.close() }
    }

    // Kid Account Management
    fun createKidAccount(username: String, password: String, age: Int, parentId: Long): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password) // In production, hash the password
            put(COLUMN_AGE, age)
            put(COLUMN_PARENT_ID, parentId)
        }
        return db.insert(TABLE_KIDS, null, values)
    }

    fun getKidByUsername(username: String): Kid? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_KIDS,
            null,
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            Kid(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                parentId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PARENT_ID))
            )
        } else null.also { cursor.close() }
    }

    // Game Progress Management
    fun saveGameProgress(kidId: Long, level: Int, score: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_KID_ID, kidId)
            put(COLUMN_LEVEL, level)
            put(COLUMN_SCORE, score)
            put(COLUMN_DATE, Date().time.toString())
        }
        db.insert(TABLE_GAME_PROGRESS, null, values)
    }

    fun getKidProgress(kidId: Long): List<GameProgress> {
        val progress = mutableListOf<GameProgress>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_GAME_PROGRESS,
            null,
            "$COLUMN_KID_ID = ?",
            arrayOf(kidId.toString()),
            null,
            null,
            "$COLUMN_DATE DESC"
        )

        while (cursor.moveToNext()) {
            progress.add(
                GameProgress(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    kidId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_KID_ID)),
                    level = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                    score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                )
            )
        }
        cursor.close()
        return progress
    }
}

// Data classes
data class Parent(
    val id: Long,
    val username: String,
    val password: String,
    val email: String
)

data class Kid(
    val id: Long,
    val username: String,
    val password: String,
    val age: Int,
    val parentId: Long
)

data class GameProgress(
    val id: Long,
    val kidId: Long,
    val level: Int,
    val score: Int,
    val date: String
)