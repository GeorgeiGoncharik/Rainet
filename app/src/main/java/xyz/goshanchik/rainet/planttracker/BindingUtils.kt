package xyz.goshanchik.rainet.planttracker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import xyz.goshanchik.rainet.R
import xyz.goshanchik.rainet.model.Plant
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("nextWateringDate")
fun TextView.nextWatering(item: Plant){


    val daysUntilWatering = item.daysTillWatering()

    text = when{
        daysUntilWatering < 0L -> "Hurry up! Your plant is drying!"
        daysUntilWatering == 0L -> "Don't forget to water it today!"
        daysUntilWatering == 1L -> "$daysUntilWatering day until watering"
        else -> "$daysUntilWatering days until watering"
    }
}

@BindingAdapter("shortenText")
fun TextView.shortenText(item: Plant){

 when {
         item.description.isEmpty() -> visibility = View.GONE
         item.description.length < 100 -> {
             visibility = View.VISIBLE
             text = item.description
         }
         else -> {
             visibility = View.VISIBLE
             "${item.description.subSequence(0, 100)}..."
         }
    }
}

@BindingAdapter("MySrc")
fun setImageViewResource(imageView: ImageView, item: Plant) {
    val file = File(item.photoUri)
    if(file.exists()){
        val myBitmap: Bitmap = BitmapFactory.decodeFile(file.toString())
        imageView.setImageBitmap(myBitmap)
    }
    else
        imageView.setImageResource(R.drawable.default_image)
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("srcPlantCondition")
fun setImageViewResourceCondition(imageView: ImageView, item: Plant) {

    val daysUntilWatering = item.daysTillWatering()

    when{
        daysUntilWatering < 0L -> {imageView.setImageResource(R.drawable.tree_bad); return;}
        (daysUntilWatering / item.wateringPeriod) < 0.5  -> {imageView.setImageResource(R.drawable.tree_medium); return;}
        (daysUntilWatering / item.wateringPeriod) > 0.5 -> {imageView.setImageResource(R.drawable.tree_good); return;}
    }

}