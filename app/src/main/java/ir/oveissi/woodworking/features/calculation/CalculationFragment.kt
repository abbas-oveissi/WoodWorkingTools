package ir.oveissi.woodworking.features.calculation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ir.oveissi.woodworking.R
import ir.oveissi.woodworking.databinding.FragmentCalculationBinding
import kotlin.math.atan


class CalculationFragment : Fragment() {


    private var _binding: FragmentCalculationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCalculationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var validation = true
                binding.alphaDegreeValue.text = "-"
                validation = validation && validateInputAndSetError(binding.aSizeInputValue, binding.aSizeInputLayout)
                validation = validation && validateInputAndSetError(binding.bSizeInputValue, binding.bSizeInputLayout)
                if (!validation) return

                val a = binding.aSizeInputValue.text.toString().toInt()
                val b = binding.bSizeInputValue.text.toString().toInt()
                val x = a.toDouble() / b.toDouble();
                binding.alphaDegreeValue.text = String.format("%.1f", Math.toDegrees(atan(x)));
            }

        }
        binding.aSizeInputValue.addTextChangedListener(textWatcher)
        binding.bSizeInputValue.addTextChangedListener(textWatcher)
    }

    private fun validateInputAndSetError(valueEditText: TextInputEditText, layout: TextInputLayout): Boolean {
        if (valueEditText.text.isNullOrEmpty()) {
            layout.error = ""
            return false
        }

        val intValue = valueEditText.text.toString().toIntOrNull()

        if (intValue == null || intValue < 0) {
            layout.error = getString(R.string.enter_valid_number)
            return false
        }

        layout.error = ""
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}