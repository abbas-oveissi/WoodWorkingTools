package ir.oveissi.woodworking.features.drawing.chainfilter

import android.graphics.Bitmap

class NoOpFilter: Filter() {
    override fun apply(image: Bitmap) =  checkNext(image)
}