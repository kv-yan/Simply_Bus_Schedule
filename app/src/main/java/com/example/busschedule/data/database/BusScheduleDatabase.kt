package com.example.busschedule.data.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.busschedule.data.dao.BusScheduleDao
import com.example.busschedule.data.model.entity.BusScheduleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

private const val TAG = "BusScheduleDatabase"

@Database(entities = [BusScheduleData::class], version = 1)
abstract class BusScheduleDatabase : RoomDatabase() {

    abstract fun busScheduleDao(): BusScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: BusScheduleDatabase? = null

        fun getDatabase(context: Context): BusScheduleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusScheduleDatabase::class.java,
                    "bus_schedule_database"
                ).addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d(
                                TAG,
                                "onOpen: is empty :: ${isTableEmpty(db, "Schedule")}",
                            )
//                            if (isTableEmpty(db, "Schedule")) {
                            copyDatabaseFromAssets(context)
//                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private fun isTableEmpty(db: SupportSQLiteDatabase, tableName: String): Boolean {
            var cursor: Cursor? = null
            return try {
                cursor = db.query("SELECT COUNT(*) FROM $tableName")
                cursor.moveToFirst()
                val count = cursor.getInt(0)
                count == 0
            } catch (ex: SQLiteException) {
                Log.e(TAG, "Error checking table: ${ex.message}")
                true
            } finally {
                cursor?.close()
            }
        }

        private fun copyDatabaseFromAssets(context: Context) {
            val dbFile = context.getDatabasePath("bus_schedule_database")
            try {
                if (dbFile.exists()) {
                    dbFile.parentFile?.mkdirs()
                    val inputStream: InputStream = context.assets.open("database/bus_schedule.db")
                    val outputStream: OutputStream = FileOutputStream(dbFile)

                    inputStream.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                Log.d(TAG, "Database copied successfully")
            } catch (ex: SQLiteException) {
                Log.e(TAG, "Error copying database: ${ex.message}")
            } catch (ex: Exception) {
                Log.e(TAG, "Unexpected error: ${ex.message}")
            }
        }
    }
}
