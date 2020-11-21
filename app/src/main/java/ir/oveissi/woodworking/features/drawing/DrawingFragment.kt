package ir.oveissi.woodworking.features.drawing

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import ir.oveissi.woodworking.databinding.FragmentDrawingBinding
import ir.oveissi.woodworking.features.drawing.chainfilter.FilterManager


class DrawingFragment : Fragment() {

    private var oldSystemBrightness: Float = 0F

    private val args: DrawingFragmentArgs by navArgs()

    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oldSystemBrightness = getBrightnessLevel().toFloat()

        binding.image.setOnTouchListener { v, event ->
            forceCollapseBottomSheet()
            if (SettingManager.rotateAndZoomEnabled) {
                return@setOnTouchListener binding.image.onTouch(v, event)
            }
            return@setOnTouchListener false
        }

        Picasso.get().load(args.imageUri.toUri()).into(binding.image)

        SettingManager.settingObservers.add {

            if (SettingManager.forceMaxBrightness) {
                refreshBrightness(1f)
            } else {
                refreshBrightness(oldSystemBrightness)
            }

            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, args.imageUri.toUri())

            val filterManager = FilterManager(SettingManager.edgeDetectionEnabled, SettingManager.flipVerticallyImage, SettingManager.flipHorizontalImage)
            binding.image.setImageBitmap(filterManager.applyFilters(bitmap))

        }

        setUpBottomSheet()
    }

    private fun setUpBottomSheet() {
        // update UI with initial values of Settings
        binding.bottomSheet.maxBrightness.isChecked = SettingManager.forceMaxBrightness
        binding.bottomSheet.rotateAndZoomEnabled.isChecked = SettingManager.rotateAndZoomEnabled

        binding.bottomSheet.maxBrightness.setOnCheckedChangeListener { _, isChecked ->
            SettingManager.forceMaxBrightness = isChecked
        }

        binding.bottomSheet.title.setOnClickListener {
            val behavior = BottomSheetBehavior.from(binding.bottomSheet.root)

            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.bottomSheet.enabledEdgeDetection.setOnCheckedChangeListener { _, isChecked ->
            SettingManager.edgeDetectionEnabled = isChecked
        }

        binding.bottomSheet.rotateAndZoomEnabled.setOnCheckedChangeListener { _, isChecked ->
            SettingManager.rotateAndZoomEnabled = isChecked
        }

        binding.bottomSheet.flipHorizontally.setOnClickListener {
            SettingManager.flipHorizontalImage = !SettingManager.flipHorizontalImage
        }

        binding.bottomSheet.flipVertically.setOnClickListener {
            SettingManager.flipVerticallyImage = !SettingManager.flipVerticallyImage
        }
    }

    private fun forceCollapseBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun getBrightnessLevel(): Double {
        return try {
            var value: Int = Settings.System.getInt(requireActivity().contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            // convert brightness level to range 0..1
            return value / 255.0
        } catch (e: Settings.SettingNotFoundException) {
            0.0
        }
    }

    private fun refreshBrightness(brightness: Float) {
        val lp = requireActivity().window.attributes
        if (brightness < 0) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            lp.screenBrightness = brightness
        }
        requireActivity().window.attributes = lp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        SettingManager.settingObservers.clear()
    }


}