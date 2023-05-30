package com.example.a12_outtake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*

class CartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        //从CartData获取购物车数据
        val foodList = CartData.items
        //更新UI
        val layoutManager = GridLayoutManager(this, 1)
        cartRecyclerView.layoutManager = layoutManager
        val adapter = CartAdapter(this, foodList)
        cartRecyclerView.adapter = adapter


        //确认订单按钮:跳转到订单页面
        cartGoToOrder.setOnClickListener{

        }
    }
}