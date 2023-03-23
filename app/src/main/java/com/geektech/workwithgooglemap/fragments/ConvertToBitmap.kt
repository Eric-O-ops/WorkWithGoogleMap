package com.geektech.workwithgooglemap.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

interface ConvertToBitmap {

    fun convert(context: Context, image: Int): BitmapDescriptor
    fun convert(view: View): Bitmap

    class Base : ConvertToBitmap {
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

        override fun convert(view: View): Bitmap {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val bitmap = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            view.draw(canvas)
            return bitmap
        }
    }
}