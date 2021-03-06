package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.today_seyebrowktver.UserData
import com.example.today_seyebrowktver.databinding.ActivityJoin2Binding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ActivityJoin2 : ActivityBase(), View.OnClickListener {

    private val REQUEST_SELECT_SHOPTYPE = 1111

    //viewBinding
    private lateinit var binding: ActivityJoin2Binding


    //data from prev Activity
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var number: String
    private lateinit var shopName: String
    private lateinit var region: String
    private lateinit var birth: String
    private lateinit var gender: String
    private lateinit var uid:String

    //data to next Activity
    private lateinit var shopType: String

    //FirebaseAuth
    var database: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference


        getDataFromPrevAct()
        setLayout()
    }

    private fun getDataFromPrevAct() {
        val intent2 = intent
        email = intent2.getStringExtra("email").toString()
        password = intent2.getStringExtra("password").toString()
        number = intent2.getStringExtra("number").toString()
        shopName = intent2.getStringExtra("shopName").toString()
        region = intent2.getStringExtra("region").toString()
        birth = intent2.getStringExtra("birth").toString()
        gender = intent2.getStringExtra("gender").toString()
    }

    private fun setLayout() {
        shopType = binding.shopTypeTv.text.toString().trim()

        binding.backIv.setOnClickListener(this)
        binding.completeBtn.setOnClickListener(this)
        binding.serviceTypeTv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backIv -> finish()

            binding.serviceTypeTv -> {
                val intent2 = Intent(applicationContext, ActivitySelectShopType::class.java)
                startActivityForResult(intent2, REQUEST_SELECT_SHOPTYPE)
            }

            binding.completeBtn -> {
                if (isValid()) {
                    mSignUp()

                }
            }
        }
    }

    fun isValid(): Boolean {
        if (shopType.startsWith("????????? ??????")) {
            mShowShortToast("????????? ??????????????????")
            return false
        } else {
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        binding.serviceTypeTv.text = data!!.getStringExtra("type")
        shopType = data!!.getStringExtra("type").toString()
        Log.d("shopType", "onResult : " + shopType)
        binding.goalEt.requestFocus()

    }

    fun mSignUp() {
        // Create a new user with a first and last name
        val user: MutableMap<String, Any> = HashMap()
        user["email"] = email
        user["password"] = password

        //?????????
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        //???????????? ?????????
                        mShowShortToast("???????????? ??????")
                        Log.d("firebaseError", "??????????????????")
                        // Sign in success, update UI with the signed-in user's information
                        //  Log.d(TAG, "createUserWithEmail:success");

                        mSignIn()
                    } else {
                        // If sign in fails, display a message to the user.
                        //???????????? ?????????
                        Log.d("firebaseError", "??????????????????")
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthUserCollisionException) {
                            mShowShortToast("????????? ????????? ????????? ???????????????")
                        } catch (e: FirebaseAuthEmailException) {
                            Log.e("error", "2" + e.message)
                        } catch (e: FirebaseAuthInvalidUserException) {
                            Log.e("error", "3" + e.message)
                        }
                    }
                })
    }

    private fun mSignIn() {
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = mAuth!!.currentUser
                    uid = user!!.uid

                    var userData = UserData(
                        email,
                        password,
                        uid,
                        shopType,
                        shopName,
                        number,
                        region,
                        birth,
                        gender,
                        "true",
                        "true",
                        binding.goalEt.text.toString().trim(),
                        binding.rcmdEt.text.toString().trim()
                    )

                    createUserData(userData)

                    val intent = Intent(this, ActivityMain::class.java)
                    startActivity(intent)
                    Log.d("firebaseError", "????????? ??????")
                }
            }
    }

    private fun createUserData(userData: UserData) {
        Log.d("firebaseError", "????????? ?????? ??????")
        database!!.child("users").child(uid).setValue(userData)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "fail : $task", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                mShowShortToast(it.message.toString())
                Log.d("firebaseError", it.message.toString())
            }.addOnCanceledListener {
                mShowShortToast("canceled")
            }
    }
}


//    private fun getDataFromPrevAct() {
//        val intent2 = intent
//        email = intent2.getStringExtra("email")
//        password = intent2.getStringExtra("password")
//    }

