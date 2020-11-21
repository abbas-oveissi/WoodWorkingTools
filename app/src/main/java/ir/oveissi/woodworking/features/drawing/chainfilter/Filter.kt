package ir.oveissi.woodworking.features.drawing.chainfilter

import android.graphics.Bitmap

abstract class Filter {
    private var next: Filter? = null

    fun linkWith(next: Filter): Filter {
        this.next = next
        return next
    }

    abstract fun apply(image: Bitmap): Bitmap

    protected fun checkNext(image: Bitmap): Bitmap {
        return if (next == null) {
            image
        } else next!!.apply(image)
    }

    companion object {

        init {
            System.loadLibrary("opencv_java")
        }
    }
}