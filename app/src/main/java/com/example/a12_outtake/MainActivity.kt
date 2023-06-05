package com.example.a12_outtake

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

//主页，食物页面

class MainActivity : AppCompatActivity() {

    //数据数组
    var foods : MutableList<Food> = mutableListOf()
    val foodList = ArrayList<Food>()
    //Food添加购物车需要监控购物车数据，把其传递给FoodAdapter
    val cartviewModel:CartViewModel by lazy { SingleCartViewModel.getCartViewModel() }
   // val cartviewModelStore = ViewModelStore()           //存储viewmodel状态，确保主页和购物车的cartviewmodel是同一个对象，md没屌用

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //首次建库时，插入菜单数据
        val dbHelper = FoodDatabaseHelper(this, FoodDatabaseHelper.DATABASE_NAME, FoodDatabaseHelper.VERSION)
        val db = dbHelper.writableDatabase
        //查询是否为空，如果空则插入
        val cursor = db.query("foodMenu", arrayOf("COUNT(*) AS count"),null,null,null,null,null)
        cursor.moveToFirst()
        val count = cursor.getInt(cursor.getColumnIndex("count"))

        Log.d("www","数据库menu表一共有${count}项")
        if(count == 0){         //空，插入数据
            for( f in menuData.valueList){
                 db.insert("foodMenu", null, f)
            }
        }


        //从数据库读取菜单数据
        val cursor2 = db.query("foodMenu", null,null,null,null,null,null)
        if(cursor2.moveToFirst()){
            var i=1
            do{
                //遍历Cursor读取数据存入到foods列表
                val id = cursor2.getInt(cursor2.getColumnIndex("id"))
                val name = cursor2.getString(cursor2.getColumnIndex("foodName"))
                val repertory = cursor2.getInt(cursor2.getColumnIndex("repertory"))
                val price = cursor2.getDouble(cursor2.getColumnIndex("price"))
                val description = cursor2.getString(cursor2.getColumnIndex("description"))

            //获取图片资源ID. getIdentifier如果没有找到资源返回0可能崩溃
                val imgName = "food"+i
                val imgID = resources.getIdentifier(imgName, "drawable",packageName)

                val f = Food(name,imgID,price,description)
                foods.add(f)        //插入foods列表，用于渲染主页列表
                i++
            }while (cursor2.moveToNext())
        }


        cursor.close()
        cursor2.close()
        db.close()

        //设置列表内容,获取购物车数据的viewmodel并传递给FoodAdapter
        initFoods()
      //  cartviewModel = ViewModelProvider(viewModelStore,ViewModelProvider.AndroidViewModelFactory(application))
       //     .get(CartViewModel::class.java)         //确保此处的cartViewModel和cartactivity的viewmodel是同一个对象
        val layoutManager = GridLayoutManager(this, 2)  //网格布局
        recyclerView.layoutManager = layoutManager
        val adapter = FoodAdapter(this, foodList, cartviewModel)     //适配器绑定
        recyclerView.adapter = adapter



        //设置自己的toolbar
        setSupportActionBar(toolbar)
        //获取ActionBar实例把左上角的返回图标显示并设为菜单图标
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        //设置菜单栏点击事件
        navView.setCheckedItem(R.id.navCall)        //默认选择
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()          //关闭

            true            //最后一行为返回值,表示事件已经被处理
        }

        //购物车按钮点击后页面进行跳转
        gotoCart.setOnClickListener{
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        //设置列表下拉刷新属性和监听器
        SwipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.abc_background_cache_hint_selector_material_dark)
        SwipeRefresh.setOnRefreshListener {
            refreshFoods(adapter)      //传入适配器用于通知列表数据已经更新
        }


    }





    //加载菜单选项
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)          //指定布局来源和目的
        return true
    }

    //定义菜单项按钮点击逻辑
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //Home按钮的id就是R.id.home,Start为位置,自动选择左或者右
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)

            R.id.backup -> Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "You clicked delete", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "You clicked setting", Toast.LENGTH_SHORT).show()

        }

        return true     //
    }


    //初始化水果
    private fun initFoods() {
        foodList.clear()
        for( x in foods){
            foodList.add(x)
        }
    }


    //刷新动作，向网络请求新的数据。
    private fun refreshFoods(adapter: FoodAdapter){
        thread {
            Thread.sleep(1500)      //模拟刷新时间，因为此处为本地数据，很快
            runOnUiThread{
                initFoods()        //重新生成数据
                adapter.notifyDataSetChanged()      //通知adapter数据已经修改
                SwipeRefresh.isRefreshing = false       //隐藏进度条，表示刷新成功
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("www","MainActivity被销毁")
    }

    override fun onStop() {
        super.onStop()
        Log.d("www","MainActivity Stop")
    }
}