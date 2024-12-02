import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "GameDB"
        const val DATABASE_VERSION = 1

        // Table names
        const val TABLE_USERS = "users"
        const val TABLE_PERFORMANCE_LOGS = "performance_logs"

        // Common columns
        const val COL_ID = "id"

        // User table columns
        const val COL_USERNAME = "username"
        const val COL_EMAIL = "email"
        const val COL_PASSWORD = "password"
        const val COL_IS_PARENT = "is_parent"
        const val COL_PARENT_ID = "parent_id"

        // Performance log columns
        const val COL_USER_ID = "user_id"
        const val COL_TIMESTAMP = "timestamp"
        const val COL_SCORE = "score"
        const val COL_GAME_LEVEL = "game_level"
        const val COL_TIME_TAKEN = "time_taken"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Users table
        db.execSQL("""
            CREATE TABLE $TABLE_USERS (
                $COL_ID TEXT PRIMARY KEY,
                $COL_USERNAME TEXT NOT NULL,
                $COL_EMAIL TEXT NOT NULL,
                $COL_PASSWORD TEXT NOT NULL,
                $COL_IS_PARENT INTEGER NOT NULL,
                $COL_PARENT_ID TEXT,
                FOREIGN KEY($COL_PARENT_ID) REFERENCES $TABLE_USERS($COL_ID)
            )
        """)

        // Performance logs table
        db.execSQL("""
            CREATE TABLE $TABLE_PERFORMANCE_LOGS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_ID TEXT NOT NULL,
                $COL_TIMESTAMP INTEGER NOT NULL,
                $COL_SCORE INTEGER NOT NULL,
                $COL_GAME_LEVEL INTEGER NOT NULL,
                $COL_TIME_TAKEN INTEGER NOT NULL,
                FOREIGN KEY($COL_USER_ID) REFERENCES $TABLE_USERS($COL_ID)
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERFORMANCE_LOGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
}