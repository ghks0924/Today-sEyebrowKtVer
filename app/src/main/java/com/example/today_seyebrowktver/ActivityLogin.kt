package com.example.today_seyebrowktver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.today_seyebrowktver.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class ActivityLogin : ActivityBase() {

    private lateinit var auth: FirebaseAuth

    //viewBinding
    private lateinit var binding: ActivityLoginBinding

    private lateinit var IDStr: String
    private lateinit var PwdStr: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(this, Color.parseColor("#fff4eb"))

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        setLayout()
    }

    private fun setLayout() {
        //아이디찾기
        binding.findIDBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ActivityFindId::class.java)
            startActivity(intent)
        })

        //비밀번호 찾기
        binding.findPwdBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ActivityFindPassword::class.java)
            startActivity(intent)
        })

        //login button event
        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if (!isVacantCheck()) {
                isValidCheck()
            } else {

            }
        })

        //join/무료체험 button event
        binding.joinBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ActivityJoin0::class.java)
            startActivity(intent)
        })
    }

    //ID, password 입력 확인
    private fun isVacantCheck(): Boolean {
        IDStr = binding.idEt.text.toString().trim()
        PwdStr = binding.passwordEt.text.toString().trim()

        if (IDStr.isNullOrEmpty()) {
            mShowShortToast("아이디를 입력해주세요")
            return true
        } else if (PwdStr.isNullOrEmpty()) {
            mShowShortToast("비밀번호를 입력해주세요")
            return true
        } else {
            return false
        }

    }

    //ID, PASSWORD 매칭 확인
    private fun isValidCheck() {
        auth.signInWithEmailAndPassword("ngh_0924@naver.com", "dnswjs12!@")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginTest", "signInWithEmail:success")
                    val intent = Intent(this, ActivityMain::class.java)
                    startActivity(intent)


                    val user = auth.currentUser
                    user.uid
                    Log.d("uid", user.uid+"?")
//                    updateUI(user)

//                    currentUser = mAuth.getCurrentUser()
//
//                    // Sign in success, update UI with the signed-in user's information
//
//                    // Sign in success, update UI with the signed-in user's information
//                    val intent = Intent(applicationContext, HomeActivity::class.java)
//                    intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
//                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            or Intent.FLAG_ACTIVITY_CLEAR_TOP) //액티비티 스택제거
//
//                    startActivity(intent)
//                    finish()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        mShowShortToast("존재하지 않는 계정 입니다")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        mShowShortToast("이메일 또는 비밀번호를 확인해주세요")
                    } catch (e: FirebaseNetworkException) {
                        mShowShortToast("네트워크 오류")
                    } catch (e: Exception) {
                        mShowShortToast("오류 : 잠시 후 다시 시도해주세요")
                    }
                }
            }
    }


    //로그인 유지상태 사용하는 메소드인데 나중에... activate
//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            reload();
//        }
//    }
}