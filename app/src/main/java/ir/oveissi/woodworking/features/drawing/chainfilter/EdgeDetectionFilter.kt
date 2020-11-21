package ir.oveissi.woodworking.features.drawing.chainfilter

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class EdgeDetectionFilter(private val enabled: Boolean): Filter() {

    override fun apply(image: Bitmap): Bitmap {
        if(enabled) {
            return checkNext(detectEdges(image))
        }
        return checkNext(image)
    }

    private fun detectEdges(bitmap: Bitmap): Bitmap {
        val rgba = Mat()
        Utils.bitmapToMat(bitmap, rgba)
        val edges = Mat(rgba.size(), CvType.CV_8UC1)
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4)
        Imgproc.medianBlur(edges, edges, 5)
        Imgproc.Canny(edges, edges, 80.0, 100.0)
        Core.bitwise_not(edges, edges)
        val resultBitmap: Bitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edges, resultBitmap)
        return resultBitmap
    }
}