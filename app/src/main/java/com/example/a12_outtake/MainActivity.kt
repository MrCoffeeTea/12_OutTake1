package com.example.a12_outtake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: FoodViewModel
    //数据数组
    lateinit var foods: MutableList<Food>

    val foodList = ArrayList<Food>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //从viewmodel获取食品数据
        viewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        foods = viewModel.foods


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

            true            //最后一行为返回值,表示时间已经被处理
        }


        //购物车按钮点击后页面进行跳转
        fab.setOnClickListener{
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }


        //设置列表内容
        initFoods()
        val layoutManager = GridLayoutManager(this, 2)  //网格布局
        recyclerView.layoutManager = layoutManager
        val adapter = FoodAdapter(this, foodList)     //适配器绑定
        recyclerView.adapter = adapter


        //设置列表下拉刷新属性和监听器
        swipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.abc_background_cache_hint_selector_material_dark)
        swipeRefresh.setOnRefreshListener {
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
                swipeRefresh.isRefreshing = false       //隐藏进度条，表示刷新成功
            }
        }
    }
}