package com.example.notestakingapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManger() {
//    databasename
    var dbName = "MyNotes"
    var dbTable = "Notes"
    //collumns
    var colID = "ID"
    var colTitle = "Title"
    var colDes= "Description"
    //database version
    var dbVersion = 1
    //create table if the table does not exist'
    val sqlCreateTable ="CREATE TABLE IF NOT EXIST" + dbTable +"("+colID+"INTEGER PRIMARY KEY,"+ colTitle+ "TEXT,"+colDes+"TEXT);"
    var sqlDB: SQLiteDatabase?=null
    constructor(context: Context) : this() {
        var db =DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }
    inner class DatabaseHelperNotes :SQLiteOpenHelper {
        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }


        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "database created..", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table if Exists" + dbTable)
        }

    }
         fun insert(values: ContentValues):Long{
             val ID =sqlDB!!.insert(dbTable, "", values)
             return ID
         }
        fun Query(pojection:Array<String>, selection:String, selectArgs:Array<String>,sorOrder:String): Cursor {
            val qb =SQLiteQueryBuilder()
            qb.tables =dbTable
            val cursor = qb.query(sqlDB,pojection, selection, selectArgs, null, null, sorOrder)
            return cursor
        }
        fun delete(selection:String, selectionArgs:Array<String>):Int{
            val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
            return count
        }
        fun update (values:ContentValues, selection: String, selectArgs: Array<String>):Int{
            val count= sqlDB!!.update(dbTable, values, selection, selectArgs)
            return  count

        }

    }
