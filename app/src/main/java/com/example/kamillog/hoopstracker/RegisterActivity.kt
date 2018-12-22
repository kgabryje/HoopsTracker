package com.example.kamillog.hoopstracker

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.kamillog.hoopstracker.models.RegisterService
import com.example.kamillog.hoopstracker.services.LoginService.Companion.mAuth
import com.example.kamillog.hoopstracker.utils.SpotsDialogHandler
import com.example.kamillog.hoopstracker.utils.ToastMessageHandler
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val spotsDialogHandler = SpotsDialogHandler(this)
    private val toastMessageHandler = ToastMessageHandler(this)
    private val registerService: RegisterService = RegisterService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener {
            register(
                registerEmailTxt.text.toString().trim(),
                registerPasswordTxt.text.toString(),
                registerConfirmPasswordTxt.text.toString()
            )
        }
    }

    fun register(email: String, pass: String, passConfirm: String) {
        if (
            TextUtils.isEmpty(email) ||
            TextUtils.isEmpty(pass) ||
            TextUtils.isEmpty(passConfirm)
        ) {
            toastMessageHandler.showToastMessage(getString(R.string.complete_fields))
            return
        }
        else if (pass != passConfirm) {
            toastMessageHandler.showToastMessage(getString(R.string.password_mismatch))
            return
        }
        else {
            registerService.register(this, mAuth, email, pass,
                {
                    spotsDialogHandler.toggleSpots(it)
                }, {
                    toastMessageHandler.showToastMessage(it)
                }, {
                    toastMessageHandler.showToastMessage(getString(R.string.verification_email))
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                })
            spotsDialogHandler.toggleSpots(true)
        }
    }
}
