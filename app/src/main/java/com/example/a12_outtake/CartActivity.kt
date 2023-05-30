package com.example.a12_outtake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

//cart刷新后导致购物车情况
//cart的删除按钮未完成

class CartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        //设置ToolBar
        setSupportActionBar(cartToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       //home键显示出来

  //失败
//        cartNavView.setOnClickListener{
//            intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//        }

        //从CartData获取购物车数据
        val foodList = CartData.items
        //更新UI
        val layoutManager = GridLayoutManager(this, 1)
        cartRecyclerView.layoutManager = layoutManager
        val adapter = CartAdapter(this, foodList)
        cartRecyclerView.adapter = adapter

        cartSwipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.abc_background_cache_hint_selector_material_dark)
        cartSwipeRefresh.setOnRefreshListener {
            refreshCart(adapter)
        }


        //确认订单按钮:跳转到订单页面
        cartGoToOrder.setOnClickListener{

        }
    }


    //刷新
    private fun refreshCart(adapter: CartAdapter){

        thread {
            Thread.sleep(2000)
            runOnUiThread {
                adapter.notifyDataSetChanged()              //adapter数据已经修改，通知
                swipeRefresh.isRefreshing = false
            }
        }

    }


}