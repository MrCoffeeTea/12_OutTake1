package com.example.a12_outtake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
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
    lateinit var adapter : CartAdapter              //tmd要注意使用同一个adapter！不如数据tmd不更新

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("www","cartActivity首次创建onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        //从CartViewModel获取购物车数据：无效
       // cviewModel = ViewModelProvider(viewModelStore,ViewModelProvider.AndroidViewModelFactory(application)).get(CartViewModel::class.java)

       // foodList = cviewModel.items
        Log.d("www","购物车获取的cviewModel的items的数量为${cviewModel.items.size}")

        //渲染购物车列表UI
        val layoutManager = GridLayoutManager(this, 1)
        cartRecyclerView.layoutManager = layoutManager
        adapter = CartAdapter(this, cviewModel.items)
        adapter.setOnItemDeleteListener(this)           //设置接口监听
        cartRecyclerView.adapter = adapter


        //设置ToolBar
        setSupportActionBar(cartToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       //home键显示出来

        //失败，无法返回
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
    override fun onFoodDeleteBtn(food: Food, num:Int, position:Int) {
        var n = num

     //   Log.d("www","现在是CartActivity，是否是UI线程：" + "${Looper.getMainLooper().thread == Thread.currentThread()}")

        if(n > 1){
            n = n-1
            cviewModel.items[food] = n
            cartFoodNum.text = " * ${n}"
            adapter.notifyItemChanged(position)     //必须更新adapter内的foodlist
            Log.d("www","${food.name}减少一个，数量n为$n")

        }else if(n == 1){
            cviewModel.items.remove(food)
            Toast.makeText(this, "删除成功！",Toast.LENGTH_SHORT).show()
            adapter.removeCartFood(position)            //更新UI：删除列表项
            Log.d("www","删除${food.name}")

        }else{
            Log.d("www","错误，删除了购物车不存在的食品")
        }

      //  cviewModel.getCartData()          //观察数据变化

    }


    //刷新，更新购物车数据,一般是没有变化的
    private fun refreshCart(adapter: CartAdapter){

        thread {
            Thread.sleep(2000)
            adapter.notifyDataSetChanged()
            swipeRefresh.isRefreshing = false
        }

    }


}