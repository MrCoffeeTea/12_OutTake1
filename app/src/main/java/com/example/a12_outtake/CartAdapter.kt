package com.example.a12_outtake


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//购物车页面RecyclerView的适配器

class CartAdapter(val context: Context, val foodMap: Map<Food, Int>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    //定义回调接口，被CartActivity继承，用于Activity更新UI。因为Adapter内无法更新UI
    interface cartDeleteListener{
        fun onFoodDeleteBtn(food: Food, num:Int, position: Int)     //接口传入被点击的Food
        fun onFoodAddOne(food: Food, position: Int)         //购物车新增1份
    }

    private var mydeletelistener : cartDeleteListener? = null        //引用回调接口

    //实现设置回调方法，用于给CartActivity把自己设置进来.因为CartActivity继承了cartDeleteListener接口，把自己这个对象传入
    //从而获得cartActivity对象(即实现该接口的对象)，通过这个对象执行在Activity中实现的接口方法
    fun setOnItemDeleteListener(itemDeleteListener: cartDeleteListener){
        mydeletelistener = itemDeleteListener
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val foodImage: ImageView = view.findViewById(R.id.foodImage)
        val foodName : TextView = view.findViewById(R.id.foodName)
        val foodPrice: TextView = view.findViewById(R.id.foodPrice)
        val foodNumber : TextView = view.findViewById(R.id.cartFoodNum)
        val deleteFood:Button = view.findViewById(R.id.deleteFood)
        val addOneFood:Button = view.findViewById(R.id.addOneFood)

    }


    //建立view视图
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_food_item, parent, false)

        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            val food = foodMap.keys.toList()[position]
            val intent = Intent(context, FoodActivity::class.java).apply {
                putExtra(FoodActivity.FOOD_NAME, food.name)
                putExtra(FoodActivity.FOOD_IMAGE_ID, food.imageId)
                putExtra(FoodActivity.FOOD_CONTENT, food.foodDescription)
                putExtra(FoodActivity.FOOD,food)
            }
            context.startActivity(intent)
        }

        return holder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val food = foodMap.keys.toList()[position]
        var num = foodMap[food]

        holder.foodName.text = food.name
        holder.foodPrice.text = "￥ ${food.price.toString()}"
        holder.foodNumber.text = " * $num"
        Glide.with(context).load(food.imageId).into(holder.foodImage)

        //删除food：数量减1或者去除
        holder.deleteFood.setOnClickListener {
            //调用回调接口的回调方法，传入被点击的food及其数量
            if (num != null) {
                Log.d("www","${holder.foodName.text}的删除按钮被点击了")
                mydeletelistener?.onFoodDeleteBtn(food,num,position)
            }
        }

        //食物数量+1
        holder.addOneFood.setOnClickListener {
            mydeletelistener?.onFoodAddOne(food,position)       //添加食物，传入food和位置索引
        }
    }


    override fun getItemCount() = foodMap.size

    //通知RecyclerView删除列表项后更新UI
    fun removeCartFood(position: Int){
        notifyItemRemoved(position)
        //Log.d("www","CartAdapter的notifyItemRemoved被调用")
    }


}