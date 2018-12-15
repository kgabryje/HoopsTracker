package com.example.kamillog.hoopstracker.models

import android.app.Activity
import android.content.Context
import com.example.kamillog.hoopstracker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class RegisterModel {
    fun register(ctx: Context,
                 mAuth: FirebaseAuth,
                 dbRef: DatabaseReference,
                 email: String,
                 pass: String,
                 onSpotsShow: (Boolean) -> Unit,
                 onError: (String) -> Unit,
                 onSuccess:() -> Unit) {

        onSpotsShow(true)
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(ctx as Activity) { task ->
                if (task.isSuccessful) {
                    val usersReference: DatabaseReference = dbRef.child("users")
                    val userId: String = mAuth.currentUser?.uid ?: ""
                    val userIdRef: DatabaseReference = usersReference.child(userId)
                    userIdRef.run{
                        child("email").setValue(email)
                        push()
                    }

                    mAuth.currentUser?.sendEmailVerification()
                    onSpotsShow(false)
                    onSuccess()

                } else {
                    onSpotsShow(false)
                    onError(ctx.getString(R.string.acc_creation_failed))
                }
            }
    }
}
