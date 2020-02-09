package com.example.findresto.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.example.findresto.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : AppCompatDialog(context) {

    private var dialogBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(dialogBinding.root)
        window!!.setLayout(WRAP_CONTENT, WRAP_CONTENT)
    }

    fun setMessage(msg: String) {
        dialogBinding.txtMessage.text = msg
    }

}