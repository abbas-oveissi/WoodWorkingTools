package ir.oveissi.woodworking.features.drawing.chainfilter

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

class FlipVerticallyFilter(private val flip: Boolean): Filter() {

    override fun apply(image: Bitmap): Bitmap {
        if(flip) {
            return checkNext(flipImage(image, -1))
        }
        return checkNext(image)
    }

    private fun flipImage(bitmap: Bitmap, flipCode: Int): Bitmap {
        val rgba = Mat()
        Utils.bitmapToMat(bitmap, rgba)
        val dst =  Mat(rgba.size(), CvType.CV_8UC1)
        Core.flip(rgba, dst, flipCode)
        val resultBitmap: Bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, resultBitmap)
        return resultBitmap
    }
}