package com.example.kamillog.hoopstracker.viewmodels

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.text.TextUtils
import com.example.kamillog.hoopstracker.R
import com.example.kamillog.hoopstracker.models.RegisterModel
import com.example.kamillog.hoopstracker.services.LoginService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel(private val context: Context) : ViewModel() {
    private val spotsLiveData = MutableLiveData<Boolean>()
    private val messageLiveData = MutableLiveData<String>()
    private val activityResultLiveData = MutableLiveData<Int>()

    fun error(): LiveData<String> = messageLiveData
    fun spotsGetter(): LiveData<Boolean> = spotsLiveData
    fun activityResultGetter(): LiveData<Int> = activityResultLiveData

    private val registerModel: RegisterModel = RegisterModel()
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth: FirebaseAuth = LoginService.mAuth

    fun register(email: String, pass: String, passConfirm: String) {
        if (
            TextUtils.isEmpty(email) ||
            TextUtils.isEmpty(pass) ||
            TextUtils.isEmpty(passConfirm)
        ) {
            messageLiveData.value = context.getString(R.string.complete_fields)
            return
        }
        else if (pass != passConfirm) {
            messageLiveData.value = context.getString(R.string.password_mismatch)
            return
        }
        else {
            registerModel.register(context, mAuth, dbRef, email, pass,
                {
                    spotsLiveData.value = it
                }, {
                    messageLiveData.value = it
                }, {
                    messageLiveData.value = context.getString(R.string.verification_email)
                    activityResultLiveData.value = Activity.RESULT_OK
                })
            spotsLiveData.value = true
        }
    }
}