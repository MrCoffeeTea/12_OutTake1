package com.example.a12_outtake


//购物车类，全局变量，用于Adapter和Cart的数据交流
object CartData{
    val items = mutableMapOf<Food,Int>()        //食物，数量
}