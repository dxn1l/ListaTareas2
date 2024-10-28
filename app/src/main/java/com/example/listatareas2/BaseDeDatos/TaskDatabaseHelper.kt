package com.example.listatareas.BaseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Task(
    val id: Int,
    val title: String?,
    val description: String?,
    val date: String?,
    val priority: String,
    val cost: Double,
    val done: Boolean
)

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "TaskDatabase.db"
        private const val TABLE_NAME = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_PRIORITY = "priority"
        private const val COLUMN_COST = "cost"
        private const val COLUMN_DONE = "done"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_DESCRIPTION TEXT,"
                + "$COLUMN_DATE TEXT,"
                + "$COLUMN_PRIORITY INTEGER,"
                + "$COLUMN_COST REAL,"
                + "$COLUMN_DONE INTEGER DEFAULT 0)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_DESCRIPTION TEXT")
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_DATE TEXT")
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_PRIORITY INTEGER")
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_COST REAL")
        }
    }

    fun addTask(title: String, description: String, date: String, priority: String, cost: Double): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_DATE, date)
            put(COLUMN_PRIORITY, priority)
            put(COLUMN_COST, cost)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COST)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DONE)) == 1
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }

    fun markTaskAsDone(id: Int, done: Boolean): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DONE, if (done) 1 else 0)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteTask(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}