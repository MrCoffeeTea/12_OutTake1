package com.example.a12_outtake

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class FoodAdapter(val context: Context, val foodList: List<Food>) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    //    ViewHolder承载列表项的布局,并被绑定了数据
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val foodImage: ImageView = view.findViewById(R.id.foodImage)
        val foodName : TextView = view.findViewById(R.id.foodName)
        val foodPrice: TextView = view.findViewById(R.id.foodPrice)
    }

    //建立view视图
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false)     //生成列表项视图

        //设置列表项点击的监听事件,实际上是给food_item.xml最外层布局添加了监听事件，在内部通过adapterposition获取具体项
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition       //获取被点击的列表项的索引
            val food = foodList[position]
            val intent = Intent(context, FoodActivity::class.java).apply {         //跳转到详情页面，并传递被点击项的数据数据
                putExtra(FoodActivity.FOOD_NAME, food.name)
                putExtra(FoodActivity.FOOD_IMAGE_ID, food.imageId)
                putExtra(FoodActivity.FOOD_CONTENT, food.foodDescription)
            }
            context.startActivity(intent)       //跳转水果详情页
        }

        return holder
    }

    //数据绑定到viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodList[position]
        holder.foodName.text = food.name
        holder.foodPrice.text = " ￥ ${food.price.toString()}"
        //load加载图片,传入url或者本地路径或者资源id,into将图片设置到一个imageView中
        Glide.with(context).load(food.imageId).into(holder.foodImage)
    }

    override fun getItemCount() = foodList.size

}