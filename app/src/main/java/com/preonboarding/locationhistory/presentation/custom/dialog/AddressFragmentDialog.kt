package com.preonboarding.locationhistory.presentation.custom.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.preonboarding.locationhistory.databinding.FragmentAddressDialogBinding

class AddressFragmentDialog(private val address: String) : DialogFragment() {

    private lateinit var binding : FragmentAddressDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddressDialogBinding.inflate(inflater, container, false)
        isCancelable = false

        initView()
        return binding.root
    }

    private fun initView() {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.addressDialogContent.text = address
        binding.addressDialogOk.setOnClickListener {
            dialog?.dismiss()
        }
        binding.addressDialogCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }
}