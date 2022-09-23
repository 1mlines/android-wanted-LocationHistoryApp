package com.preonboarding.locationhistory.common.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

class DialogUtil<T: ViewDataBinding>(
    context: Context,
    @LayoutRes private val layoutRes: Int,
) : Dialog(context){

    private val builder = AlertDialog.Builder(context)

    fun initDialog(): T {
        val inflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<T>(inflater, layoutRes, null, false)

        (context as Window).setContentView(layoutRes)

        return binding
    }

}