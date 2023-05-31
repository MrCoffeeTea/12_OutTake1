package com.example.a12_outtake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_food_item.*
import kotlin.concurrent.thread

//cart刷新后导致购物车情况
//cart的删除按钮未完成

//继承CartAdapter的删除食品接口
class CartActivity : AppCompatActivity(), CartAdapter.cartDeleteListener {

    lateinit var foodList: MutableMap<Food,Int>
   // lateinit var cviewModel: CartViewModel
    val cviewModel : CartViewModel by lazy { SingleCartViewModel.getCartViewModel() }
    val cviewModelStore = ViewModelStore()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("www","cartActivity首次创建onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        //从CartViewModel获取购物车数据：无效
       // cviewModel = ViewModelProvider(viewModelStore,ViewModelProvider.AndroidViewModelFactory(application))
        //    .get(CartViewModel::class.java)

        foodList = cviewModel.items
        Log.d("www","购物车的foodList数量为${foodList.size}和${cviewModel.items.size},id为${cviewModel.id}")

        //更新UI
        val layoutManager = GridLayoutManager(this, 1)
        cartRecyclerView.layoutManager = layoutManager
        val adapter = CartAdapter(this, foodList)
        adapter.setOnItemDeleteListener(this)           //设置接口监听
        cartRecyclerView.adapter = adapter



        //设置ToolBar
        setSupportActionBar(cartToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       //home键显示出来

        //失败
        cartNavView.setOnClickListener{
            intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



        //刷新
        cartSwipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.abc_background_cache_hint_selector_material_dark)
        cartSwipeRefresh.setOnRefreshListener {
            refreshCart(adapter)
        }


        //确认订单按钮:跳转到订单页面
        cartGoToOrder.setOnClickListener{

        }
    }


    //一次只能减少1，删除列表要点击数量后才删除
    override fun onFoodDeleteBtn(food: Food, num:Int) {
        var n = num         //传入的参数num无法更改值

        //runOnUiThread线程执行慢，多次点击删除更改CartData时只减少了数量1次。原本数量大于5，此时继续删除至1后无法继续删除
        runOnUiThread{
            if(n > 1){
                n = n-1
                cartFoodNum.text = " * ${n}"
                cviewModel.items[food] = n
                Log.d("www","${food.name}减少1，数量n为$n")
            }else{
                cviewModel.items.remove(food)
                Toast.makeText(this, "删除成功！",Toast.LENGTH_SHORT).show()
                //删除？如何
                Log.d("www","删除${food.name}")
            }
        }

    }


    //刷新
    private fun refreshCart(adapter: CartAdapter){

        thread {
            Thread.sleep(2000)
           // runOnUiThread {
                adapter.notifyDataSetChanged()              //adapter数据已经修改，通知
                swipeRefresh.isRefreshing = false
           // }
        }

    }


}