package com.example.kamillog.hoopstracker.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kamillog.hoopstracker.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserViewModel : ViewModel() {
    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val userModelLiveData = MutableLiveData<UserModel>()

    fun userModel() = userModelLiveData

    fun setUserData() {

        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userUid = mAuth.currentUser?.uid ?: ""
                if (!snapshot.hasChild(userUid))
                    return
                val userEmail = snapshot.child(userUid).child("email").value.toString()
                userModelLiveData.value = UserModel(userEmail)
            }
        })
    }

}