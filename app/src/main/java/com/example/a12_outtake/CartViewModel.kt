package com.example.a12_outtake

import android.util.Log
import androidx.lifecycle.ViewModel



//object CartData{
//    val items = mutableMapOf<Food,Int>()        //食物，数量
//}

//存储购物车数据
class CartViewModel:ViewModel(){

    val items = mutableMapOf<Food,Int>()
    var id = 1

    fun getCartData(){
        Log.d("www","items大小为${items.size}")
        items.forEach{
            (k,v) -> Log.d("www","${k.name}:$v-----")
        }
    }
}