package com.example.kamillog.hoopstracker.utils

import android.content.Context
import com.example.kamillog.hoopstracker.R
import dmax.dialog.SpotsDialog

class SpotsDialogHandler(private val ctx: Context) {
    private val spotsDialog: SpotsDialog by lazy {
        SpotsDialog(ctx, R.style.DialogStyleReg)
    }

    fun toggleSpots(state: Boolean?) {
        if(state == true)
            spotsDialog.show()
        else
            spotsDialog.dismiss()
    }
}