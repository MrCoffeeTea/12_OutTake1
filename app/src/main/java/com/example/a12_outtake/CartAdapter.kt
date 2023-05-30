package com.example.a12_outtake


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


class CartAdapter(val context: Context, val foodList: Map<Food, Int>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val foodImage: ImageView = view.findViewById(R.id.foodImage)
        val foodName : TextView = view.findViewById(R.id.foodName)
        val foodPrice: TextView = view.findViewById(R.id.foodPrice)
        val foodNumber : TextView = view.findViewById(R.id.cartFoodNum)
        val deleteFood:Button = view.findViewById(R.id.deleteFood)

    }


    //建立view视图
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_food_item, parent, false)

        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            val food = foodList.keys.toList()[position]
            val intent = Intent(context, FoodActivity::class.java).apply {
                putExtra(FoodActivity.FOOD_NAME, food.name)
                putExtra(FoodActivity.FOOD_IMAGE_ID, food.imageId)
                putExtra(FoodActivity.FOOD_CONTENT, food.foodDescription)
            }
            context.startActivity(intent)
        }

        return holder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val food = foodList.keys.toList()[position]
        var num = foodList[food]

        holder.foodName.text = food.name
        holder.foodPrice.text = "￥ ${food.price.toString()}"
        holder.foodNumber.text = " * $num"
        Glide.with(context).load(food.imageId).into(holder.foodImage)

        //删除food：数量减1或者去除
        holder.deleteFood.setOnClickListener {


//            if(holder.foodNumber.text.toString().toInt() > 1){
//                num?.minus(1)
//                holder.foodNumber.text = " * ${num}"
//            }else{
//                CartData.items.remove(food)        //删除food
//                Toast.makeText(holder.itemView.context, "成功删除", Toast.LENGTH_SHORT).show()
//            }
            CartData.items.remove(food)
            Toast.makeText(holder.itemView.context, "成功删除", Toast.LENGTH_SHORT).show()
            //如何修改UI把food从购物车删除呢？

        }
    }



    override fun getItemCount() = foodList.size

}