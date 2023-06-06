package com.example.a12_outtake

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlin.random.Random


//食品详情页面
class FoodActivity : AppCompatActivity() {


    companion object{
        const val FOOD_NAME = "food_name"
        const val FOOD_IMAGE_ID = "food_image_id"
        const val FOOD_CONTENT = "food_content"
        const val FOOD_PRICE = "food_price"
        const val FOOD = "food"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        //通过Intent获取被点击食品的food对象
        val food = intent.getParcelableExtra<Food>(FOOD)
        val foodName = food?.name
        val foodImageId = food?.imageId
        val foodDescription = food?.foodDescription
        val foodPrice = food?.price

        setSupportActionBar(toolbar)        //设定可折叠的加强版toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //打开home键
        collapsingToolbar.title = foodName
        Glide.with(this).load(foodImageId).into(foodImageView)        //加载传入的食品图片，位置设置到标题栏上foodImageView
      //  foodContentText.text = generateFoodContent(foodName)

        foodContentName.setText("$foodName")
        foodContentText.setText(foodDescription)
        newPrice.setText("￥ ${foodPrice}")                                    //新价格
        oldPrice.setText("${generateOldPrice()}")
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);          //旧价格添加删除线

        foodPagePutInCart.setOnClickListener {

            val cviewModel : CartViewModel by lazy { SingleCartViewModel.getCartViewModel() }
            Log.d("www","--------${cviewModel.id}")
            if(food != null)
               if(cviewModel.items[food] == null)           //如果没有这个商品，新增。否则数量加1
                   cviewModel.items.put(food,1)
                else
                    cviewModel.items[food] = cviewModel.items[food]!!.inc()

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {          //当点击了home，关闭当前activity，从而返回上一个activity
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

//随机生成旧的价格
    fun generateOldPrice():String{
        val minValue = 100
        val maxValue = 2000
        val x = Random.nextDouble(maxValue - minValue + 1.0 ) + minValue
        return String.format("%.2f",x)
    }
}