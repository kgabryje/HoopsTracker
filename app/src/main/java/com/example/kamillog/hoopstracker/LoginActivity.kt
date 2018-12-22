package com.example.kamillog.hoopstracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.services.LoginService.Companion.dbRef
import com.example.kamillog.hoopstracker.services.LoginService.Companion.mAuth
import com.example.kamillog.hoopstracker.utils.SpotsDialogHandler
import com.example.kamillog.hoopstracker.utils.ToastMessageHandler
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 1
        const val REGISTER_KEY = 2
    }

    private lateinit var mSignInClient: GoogleSignInClient
    private val spotsDialogHandler = SpotsDialogHandler(this)
    private val toastMessageHandler = ToastMessageHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mSignInClient = LoginService.initGoogleApiClient(this)

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
        LoginService.mAuth.addAuthStateListener(LoginService.initFirebaseAuthListener { startHomeActivity() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                spotsDialogHandler.toggleSpots(false)
            }
        }
        else if (requestCode == REGISTER_KEY && resultCode == Activity.RESULT_OK) {
            toastMessageHandler.showToastMessage(getString(R.string.register_succeeded))
        }
    }

    private fun startHomeActivity(){
        startActivity(Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    private fun signInWithEmailAndPassword(email: String, pass: String) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            toastMessageHandler.showToastMessage(getString(R.string.incorrect_credentials))
            return
        } else {
            LoginService.login(this, email, pass, {
                spotsDialogHandler.toggleSpots(it)
            }, {
                toastMessageHandler.showToastMessage(it)
            }, {
                startHomeActivity()
            })
        }
    }

    private fun signInWithGoogle() {
        spotsDialogHandler.toggleSpots(true)
        mSignInClient.signInIntent
        startActivityForResult(mSignInClient.signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task->

                if(task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid ?: ""
                    val acct = GoogleSignIn.getLastSignedInAccount(this)
                    dbRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if(!p0.hasChild(userId) && acct != null){
                                val personEmail = acct.email
                                dbRef.child(userId).run {
                                    child("email").setValue(personEmail)
                                    push()
                                }
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            spotsDialogHandler.toggleSpots(false)
                        }
                    })
                } else {
                    toastMessageHandler.showToastMessage(getString(R.string.connection_error))
                }
                spotsDialogHandler.toggleSpots(false)

            }
    }
}
