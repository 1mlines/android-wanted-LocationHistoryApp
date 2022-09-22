package com.preonboarding.locationhistory.presentation.custom.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.preonboarding.locationhistory.databinding.AddressDialogBinding

class AddressDialog(private val context: AppCompatActivity) {

    private lateinit var binding: AddressDialogBinding
    private val dlg = Dialog(context)

    fun show(content: String) {
        binding = AddressDialogBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(binding.root)
        dlg.setCancelable(false)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.addressDialogContent.text = content

        binding.addressDialogOk.setOnClickListener {
            dlg.dismiss()
        }

        binding.addressDialogCancel.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }
}
