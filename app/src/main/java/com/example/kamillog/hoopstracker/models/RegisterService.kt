package com.example.kamillog.hoopstracker.models

import android.app.Activity
import android.content.Context
import com.example.kamillog.hoopstracker.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterService {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

    fun register(ctx: Context,
                 mAuth: FirebaseAuth,
                 email: String,
                 pass: String,
                 onSpotsShow: (Boolean) -> Unit,
                 onError: (String) -> Unit,
                 onSuccess:() -> Unit) {

        onSpotsShow(true)
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(ctx as Activity) { task ->
                try {
                    val userId = task.getResult(FirebaseException::class.java)!!.user.uid
                    val userIdRef: DatabaseReference = dbRef.child(userId)
                    userIdRef.run{
                        child("email").setValue(email)
                        push()
                    }

                    mAuth.currentUser?.sendEmailVerification()
                    onSpotsShow(false)
                    onSuccess()
                } catch (e: FirebaseException) {
                    onSpotsShow(false)
                    onError(e.message ?: ctx.getString(R.string.acc_creation_failed))
                }
            }
    }
}
