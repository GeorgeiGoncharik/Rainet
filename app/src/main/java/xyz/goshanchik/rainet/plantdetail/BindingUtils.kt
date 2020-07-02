package xyz.goshanchik.rainet.plantdetail

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import xyz.goshanchik.rainet.model.Plant
//
//@BindingAdapter("SetSrc")
//fun ImageView.setSrc(item: Plant{
//    if(item.photoUri.isNotEmpty())
//
//})


@BindingAdapter("NumToStringAsText")
fun Button.NumToString(num: Number){
    text = num.toString()
}