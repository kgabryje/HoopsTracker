package com.example.kamillog.hoopstracker

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kamillog.hoopstracker.utils.SpotsDialogHandler
import com.example.kamillog.hoopstracker.utils.ToastMessageHandler
import com.example.kamillog.hoopstracker.viewmodels.LoginViewModel
import com.example.kamillog.hoopstracker.viewmodels.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel : LoginViewModel

    companion object {
        const val RC_SIGN_IN = 1
        const val REGISTER_KEY = 2
    }

    private val spotsDialogHandler = SpotsDialogHandler(this)
    private val toastMessageHandler = ToastMessageHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, "Login"))
            .get(LoginViewModel::class.java)
        viewModel.startNewActivity().observe(this, Observer(this::startHomeActivity))
        viewModel.spotsGetter().observe(this, Observer(spotsDialogHandler::toggleSpots))
        viewModel.error().observe(this, Observer(toastMessageHandler::showToastMessage))

        registerText.setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java),REGISTER_KEY)
        }

        loginBtn.setOnClickListener {
            signInWithEmailAndPassword(emailText?.text.toString().trim(), passwordText?.text.toString().trim())
        }

        googleBtn.setOnClickListener {
            signInWithGoogle()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startListening()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    viewModel.firebaseAuthWithGoogle(account, this)
                }
            } catch (e: ApiException) {
                spotsDialogHandler.toggleSpots(false)
            }
        }
        else if(requestCode == REGISTER_KEY && resultCode == Activity.RESULT_OK)
        {
            toastMessageHandler.showToastMessage(getString(R.string.register_succeeded))
        }
    }

    private fun startHomeActivity(state: Boolean?){
        if (state == true) {
            startActivity(
                Intent(
                    this, FollowTeamsActivity::class.java // todo: change to HomeActivity
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }

    private fun signInWithEmailAndPassword(email: String, pass: String) {
        viewModel.signInWithEmailAndPassword(email,pass,this)
    }

    private fun signInWithGoogle() {
        startActivityForResult(viewModel.signInWithGoogle(), RC_SIGN_IN)
    }
}
