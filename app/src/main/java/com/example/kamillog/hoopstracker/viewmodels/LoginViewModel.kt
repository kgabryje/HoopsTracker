package com.example.kamillog.hoopstracker.viewmodels

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*

class LoginViewModel(private val ctx: Context) : ViewModel() {
    private val errorLiveData = MutableLiveData<String>()
    private val newActivity = MutableLiveData<Boolean>()
    private val spotsLiveData = MutableLiveData<Boolean>()

    fun error(): LiveData<String> = errorLiveData
    fun startNewActivity(): LiveData<Boolean> = newActivity
    fun spotsGetter(): LiveData<Boolean> = spotsLiveData

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
    private val loginService = LoginService()

    private val mAuth = LoginService.mAuth

    private val mListener: FirebaseAuth.AuthStateListener by lazy {
        loginService.initFirebaseAuthListener {
            newActivity.value = true
            print(mAuth.currentUser!!.displayName)
        }
    }

    private val mGoogleApiClient: GoogleApiClient by lazy {
        loginService.initGoogleApiClient(ctx) {
            errorLiveData.value = it
        }
    }

    fun signInWithGoogle() : Intent {
        spotsLiveData.value = true
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
    }

    fun startListening(){
        mAuth.addAuthStateListener(mListener)
    }

    fun signInWithEmailAndPassword(email: String, pass: String, context: Context) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            errorLiveData.value = context.getString(R.string.incorrect_credentials)
            return
        } else {
            loginService.login(context, email, pass, {
                spotsLiveData.value = it
            }, {
                errorLiveData.value = it
            }, {
                newActivity.value = true
                print(mAuth.currentUser!!.displayName)
            })
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount, ctx: Context) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(ctx as Activity) { task->

                if(task.isSuccessful) {
                    val userId: String = mAuth.currentUser?.uid ?: ""
                    val acct = GoogleSignIn.getLastSignedInAccount(ctx)
                    dbRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if(!p0.hasChild(userId)){
                                if (acct != null ) {
                                    val personEmail = acct.email
                                    dbRef.child(userId).run {
                                        child("Email").setValue(personEmail)
                                        push()
                                    }
                                }
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("Logout", "error!")
                            spotsLiveData.value = false
                        }
                    })
                    spotsLiveData.value = false
                    newActivity.value = true
                    print(mAuth.currentUser!!.displayName)
                } else {
                    spotsLiveData.value = false
                    errorLiveData.value = ctx.getString(R.string.connection_error)
                }
            }
    }
}