package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kamillog.hoopstracker.utils.SpotsDialogHandler
import com.example.kamillog.hoopstracker.utils.ToastMessageHandler
import com.example.kamillog.hoopstracker.viewmodels.RegisterViewModel
import com.example.kamillog.hoopstracker.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private val spotsDialogHandler = SpotsDialogHandler(this)
    private val toastMessageHandler = ToastMessageHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, "Register"))
            .get(RegisterViewModel::class.java)
        viewModel.error().observe(this, Observer(toastMessageHandler::showToastMessage))
        viewModel.spotsGetter().observe(this, Observer(spotsDialogHandler::toggleSpots))
        viewModel.activityResultGetter().observe(this, Observer {
            it?.let { setResult(it, Intent()) }
            finish()
        })

        registerBtn.setOnClickListener {
            viewModel.register(
                registerEmailTxt.text.toString().trim(),
                registerPasswordTxt.text.toString(),
                registerConfirmPasswordTxt.text.toString()
            )
        }
    }
}
