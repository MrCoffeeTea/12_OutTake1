package com.example.a12_outtake

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

//数据库助手类: 上下文，数据库名称，版本号
class FoodDatabaseHelper(val context: Context, name:String, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    //建立菜品列表:id，菜名，库存，价格，描述
    private val createMenu = "create table foodMenu (id integer primary key autoincrement, " +
            "foodName text, repertory integer, price real, description text )"

    //建立订单列表：id，外卖单号，食品名称，食品数量，食品单价。id为主键，同一个orderID表示同一份外卖的食品
    private val createOrderList = "create table OrderList (id integer primary key autoincrement, " +
            "orderID integer, foodName text, amount integer, price real )"


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createOrderList)
        Toast.makeText(context, "订单列表的数据表建立成功", Toast.LENGTH_SHORT).show()
        Log.d("www","订单列表的数据表建立成功")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


}