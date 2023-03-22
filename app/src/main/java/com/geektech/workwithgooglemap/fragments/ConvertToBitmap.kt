package com.geektech.workwithgooglemap.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

interface ConvertToBitmap {

    fun convert(context: Context, image: Int): BitmapDescriptor

    class Base: ConvertToBitmap {
        override fun convert(context: Context, image: Int): BitmapDescriptor {
            val vectorDrawable = ContextCompat.getDrawable(context, image)
            vectorDrawable?.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )
            val bitmap = Bitmap.createBitmap(
                vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}