package com.canbazdev.hmskitsproject1.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.canbazdev.hmskitsproject1.R

/*
*   Created by hamzacanbaz on 7/28/2022
*/
class LoadingDialog(val context: Context, val message: String) {
    lateinit var dialog: AlertDialog
    fun startLoadingDialog() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        val builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        view.findViewById<TextView>(R.id.tvInfo).text = message
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

}

class ChooseDialog(val context: Context, val title: String) {
    lateinit var dialog: AlertDialog
    fun startDialog() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_ask, null);
        val builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)

        dialog = builder.create()
        builder.setTitle(title)
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}