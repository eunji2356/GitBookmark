package kr.co.chooji.gitbookmark.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception
import java.sql.SQLException

open class DBHelper(
    context: Context,
    dbName: String,
    dbVersion: Int
): SQLiteOpenHelper(context, dbName, null, dbVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        val tableList = DBTable.getCreateTable()

        try{
            for (table in tableList){
                db.execSQL(table)
            }
        }
        catch (e: SQLException){
            Log.e("#### SQLException", "테이블 생성 중 오류가 발생했습니다.\n $e")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try{
            onCreate(db)
        }
        catch (e: Exception){
            Log.e("#### onUpgrade Error", "DB 업그레이드 중 오류가 발생했습니다.\n $e")
        }
    }

}