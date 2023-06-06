package com.example.a12_outtake

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel

//食物类
class Food (val name:String, val imageId:Int=0, val price:Double=10.0, val foodDescription:String="") : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(imageId)
        parcel.writeDouble(price)
        parcel.writeString(foodDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }

}