package ir.oveissi.woodworking.features.drawing.chainfilter

import android.graphics.Bitmap

class FilterManager(edgeDetectionEnabled: Boolean, flipVerticallyImage: Boolean, flipHorizontalImage: Boolean) {

    // create chain from out Filters
    private val filterChain: Filter = NoOpFilter()

    init {
        filterChain.linkWith(EdgeDetectionFilter(edgeDetectionEnabled))
            .linkWith(FlipHorizontallyFilter(flipHorizontalImage))
            .linkWith(FlipVerticallyFilter(flipVerticallyImage))
    }

    fun applyFilters(image: Bitmap): Bitmap {
        return filterChain.apply(image)
    }

}