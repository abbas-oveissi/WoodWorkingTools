package ir.oveissi.woodworking.features.home

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import ir.oveissi.woodworking.BuildConfig
import ir.oveissi.woodworking.R
import ir.oveissi.woodworking.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickImages: ActivityResultLauncher<String>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private fun registerActivityResultCallbacks() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                pickImageFromGallery()
            }
        }
        pickImages = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val action = HomeFragmentDirections.actionHomeFragmentToDrawingFragment(uri.toString())
            findNavController().navigate(action)
        }
    }

    private fun pickImageFromGallery() {
        pickImages.launch("image/*")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerActivityResultCallbacks()

        binding.drawModel.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        binding.calculation.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCalculationFragment())
        }

        binding.version.text = getString(R.string.version) + " " + BuildConfig.VERSION_NAME

    }

}