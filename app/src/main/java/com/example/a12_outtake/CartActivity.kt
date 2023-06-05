package com.example.a12_outtake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_food_item.*
import kotlin.concurrent.thread

//购物车页面

//继承CartAdapter的删除食品接口
class CartActivity : AppCompatActivity(), CartAdapter.cartDeleteListener {

    lateinit var foodList: MutableMap<Food,Int>
    val cviewModel : CartViewModel by lazy { SingleCartViewModel.getCartViewModel() }
    val cviewModelStore = ViewModelStore()
    lateinit var adapter : CartAdapter              //tmd要注意使用同一个adapter！不然数据tmd不更新

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("www","cartActivity首次创建onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        //从CartViewModel获取购物车数据：无效
       // cviewModel = ViewModelProvider(viewModelStore,ViewModelProvider.AndroidViewModelFactory(application)).get(CartViewModel::class.java)

       // Log.d("www","购物车获取的cviewModel的items的数量为${cviewModel.items.size}")

        //渲染购物车列表UI
        val layoutManager = GridLayoutManager(this, 1)
        cartRecyclerView.layoutManager = layoutManager
        adapter = CartAdapter(this, cviewModel.items)
        adapter.setOnItemDeleteListener(this)                   //设置接口监听
        cartRecyclerView.adapter = adapter


        //设置ToolBar
        setSupportActionBar(cartToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       //home键显示出来


        //初始化底部总价格
        cartSum?.setText(updateCartSum(cviewModel.items))


        //刷新
        cartSwipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.abc_background_cache_hint_selector_material_dark)
        cartSwipeRefresh.setOnRefreshListener {
            refreshCart(adapter)
        }


        //确认订单按钮:跳转到订单页面
        cartGoToOrder.setOnClickListener{

        }

        //清空购物车
        clearCartBtn.setOnClickListener {
            clearCart()
        }

    }


    //删除按钮，删除viewmodel的数据后通知adapter数据修改，进而重新渲染页面
    override fun onFoodDeleteBtn(food: Food, num:Int, position:Int) {
        var n = num
     //   Log.d("www","现在是CartActivity，是否是UI线程：" + "${Looper.getMainLooper().thread == Thread.currentThread()}")
        if(n > 1){
            n = n-1
            cviewModel.items[food] = n
            cartFoodNum.text = " * ${n}"
            //adapter.notifyItemChanged(position)     //必须更新adapter内的foodlist
            adapter.notifyDataSetChanged()
            Log.d("www","${food.name}减少一个，数量n为$n")

        }else if(n == 1){
            cviewModel.items.remove(food)
            Toast.makeText(this, "删除成功！",Toast.LENGTH_SHORT).show()
            //adapter.removeCartFood(position)            //更新UI：删除列表项
            adapter.notifyDataSetChanged()
            Log.d("www","删除${food.name}")

        }else{
            Log.d("www","错误，删除了购物车不存在的食品")
        }

        //修改总金额
        cartSum.setText(updateCartSum(cviewModel.items))

       // cviewModel.getCartData()          //控制台观察数据变化
       // Log.d("www","\n")
    }

    //增加1份食物
    override fun onFoodAddOne(food: Food, position: Int) {

        val n : Int? = cviewModel.items[food]?.inc()
        cviewModel.items[food] = n?:0        //数量加1
        cartFoodNum.text = " * ${n}"
        adapter.notifyDataSetChanged()
        Log.d("www","${food.name}增加一个，有${n}个")

        //修改总金额
        cartSum.setText(updateCartSum(cviewModel.items))
    }


    //刷新，只是一个效果，没有进行数据请求。控制栏判断了购物车是否空
    private fun refreshCart(adapter: CartAdapter){

        thread {
            Thread.sleep(2000)
          //  adapter.notifyDataSetChanged()
            cartSwipeRefresh.isRefreshing = false

        }
        Log.d("www","购物车是否为空${if(isCartEmpty()!=0) "非空" else "空" }")
    }

    //菜单项按钮的点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> startActivity(Intent(this,MainActivity::class.java))
        }

        return true
    }


    //计算总价格
    fun updateCartSum(m : MutableMap<Food,Int>):String{
       // Log.d("www","计算总价中...总共有${m.size}项")
        var sumPrice : Double = 0.0

        m.forEach{ (_Food,_num) ->

             if(_num != 0){
                  sumPrice += _Food.price*_num
               //  Log.d("www","${_Food.name},有${_num}个，每个${_Food.price}元")
             }
          //  Log.d("www","${_Food.name}数类型${_num}")

        }

        return String.format("%.2f",sumPrice)
    }


    //清空购物车
    fun clearCart(){
        Log.d("www","cartActivity通知要清空购物车了")
        cviewModel.items.clear()
        adapter.notifyDataSetChanged()
    }


    //判断购物车是否为空,0表示空。
    fun isCartEmpty() : Int{
        var flag = 0
        cviewModel.items.forEach{
            (k,v)->
            if(v != 0)
                flag = 1
        }
        return flag
    }


    //测试用
    override fun onPause() {
        super.onPause()
        adapter.notifyDataSetChanged()
        Log.d("www","CartActivity进入Pause状态，cviewModel.items大小为： ${cviewModel.items.size}")
    }

    override fun onStop() {
        super.onStop()
        adapter.notifyDataSetChanged()
        Log.d("www","CartActivity进入Stop状态，cviewModel.items大小为： ${cviewModel.items.size}")

    }

}