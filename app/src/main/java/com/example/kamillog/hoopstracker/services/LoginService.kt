package com.example.kamillog.hoopstracker.services

import android.app.Activity
import android.content.Context
import com.example.kamillog.hoopstracker.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginService {
    companion object {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")


        fun initGoogleApiClient(ctx: Context): GoogleSignInClient {
            val options: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ctx.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(ctx, options)
        }

        fun initFirebaseAuthListener(onSuccess: () -> Unit): FirebaseAuth.AuthStateListener =
            FirebaseAuth.AuthStateListener { auth ->
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    onSuccess()
                }
            }

        fun login(
            ctx: Context, email: String, pass: String,
            onSpotsShow: (Boolean) -> Unit,
            onError: (String) -> Unit,
            onSuccess: () -> Unit
        ) {
            onSpotsShow(true)
            mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(ctx as Activity) { task ->
                    onSpotsShow(false)
                    if (task.isSuccessful) {
                        if (mAuth.currentUser?.isEmailVerified != true) {
                            onError(ctx.getString(R.string.email_verify_prompt))
                        } else {
                            onSuccess()
                        }
                    } else {
                        onError(ctx.getString(R.string.incorrect_credentials))
                    }
                }
        }

        fun logout(mAuth: FirebaseAuth, onSuccess: () -> Unit) {
            mAuth.signOut()
            onSuccess()
        }

        fun signOut() {
            mAuth.signOut()
        }
    }
}