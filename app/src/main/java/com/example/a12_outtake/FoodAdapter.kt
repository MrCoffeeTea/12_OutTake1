package com.example.a12_outtake

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//首页食物RecyclerView的适配器

//把购物车的viewmodel作为参数传入
class FoodAdapter(val context: Context, val foodList: List<Food>, private val _cartViewModel: CartViewModel) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    val cartViewModel:CartViewModel by lazy { SingleCartViewModel.getCartViewModel() }

    //ViewHolder承载列表项的布局,并被绑定了数据
    //列表项的数据和内容在这里找到赋值给holder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val foodImage: ImageView = view.findViewById(R.id.foodImage)
        val foodName : TextView = view.findViewById(R.id.foodName)
        val foodPrice: TextView = view.findViewById(R.id.foodPrice)
        val putInCartBtn : Button = view.findViewById(R.id.putInCart)
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
                putExtra(FoodActivity.FOOD_PRICE, food.price)
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


        //加入购物车,数据传递到CartActivity
        holder.putInCartBtn.setOnClickListener {
            Toast.makeText(holder.itemView.context,"成功添加",Toast.LENGTH_SHORT).show()
            //Log.d("www","此时cartviewmodel数量为：${cartViewModel.items.size}")
            cartViewModel.id = 2        //测试用


            //如果存在则数量+1，否则插入
            if(cartViewModel.items.containsKey(food)){
                cartViewModel.items.put(food,cartViewModel.items.getValue(food)+1)
                Log.d("www","${food.name}数量加一，此时数量为${cartViewModel.items[food]}.")

            }else{
                cartViewModel.items.put(food,1)
                Log.d("www","${food.name}添加成功,此时数量为1")
                //Log.d("www","此时cartviewmodel数量为：${cartViewModel.items.size}")
            }
        }

    }

    override fun getItemCount() = foodList.size

}



















