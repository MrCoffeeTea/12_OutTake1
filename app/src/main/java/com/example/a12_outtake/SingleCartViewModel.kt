package com.example.a12_outtake


//它保存了CartViewModel的实例，FoodActivity和CartActivity都从此获取CartViewModel
// 确保获取的是同一个viewmodel，以此确保购物车数据共享
object SingleCartViewModel {
    val cViewModel by lazy { CartViewModel() }

    fun getCartViewModel():CartViewModel{
        return cViewModel
    }
}