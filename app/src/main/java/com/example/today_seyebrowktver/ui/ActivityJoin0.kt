package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.today_seyebrowktver.databinding.ActivityJoin0Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.regex.Pattern

class ActivityJoin0 : ActivityBase(), View.OnClickListener {

    //viewBinding
    private lateinit var binding: ActivityJoin0Binding

    private lateinit var emailStr: String
    private lateinit var passwordStr: String
    private lateinit var rePasswordStr: String

    val database = FirebaseDatabase.getInstance().reference
    var auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoin0Binding.inflate(layoutInflater)
        setContentView(binding.root)


        setLayout()
    }

    private fun setLayout() {
        binding.backIv.setOnClickListener(this)
        binding.nextTv.setOnClickListener(this)

        //초기화
        emailStr = binding.idEt.text.toString().trim()
        passwordStr = binding.pwdEt.text.toString().trim()
        rePasswordStr = binding.rePwdEt.text.toString().trim()

        //test
        binding.idEt.setText("test1@test.com")
        binding.pwdEt.setText("123qwe")
        binding.rePwdEt.setText("123qwe")

        //ID 입력 확인
        binding.idEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                emailStr = binding.idEt.text.toString().trim()
                if (!isValidEmail(emailStr)) {
                    binding.idTv.setTextColor(Color.parseColor("#ea562e"))
                } else {
                    binding.idTv.setTextColor(Color.parseColor("#804f4f4f"))

                }
            }

        })

        //password 입력
        binding.pwdEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordStr = binding.pwdEt.text.toString().trim()
                if (!isValidPasswd(passwordStr)) {
                    binding.validPwdTv.visibility = View.VISIBLE
                } else {
                    binding.validPwdTv.visibility = View.GONE
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.rePwdEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rePasswordStr = binding.rePwdEt.text.toString().trim()
                if (passwordStr == rePasswordStr) {
                    binding.pwdMatchTv.visibility = View.GONE
                } else {
                    binding.pwdMatchTv.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backIv -> {
                finish()
            }

            binding.nextTv -> {
                if (isValidToNext()) {
                    emailDuplicateCheck()
                } else {

                }

            }

        }
    }


    //===================입력값 유효성 검사=====================

    private fun isValidEmail(target: String?): Boolean {
        return if (target == null || TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    private fun isValidPasswd(target: String): Boolean {
        if (Pattern.matches("(^.*(?=.{6,100})(?=.*[0-9])(?=.*[a-zA-Z]).*$)", target)) {
            return true
        } else {
            return false
        }
    }

    private fun isValidToNext(): Boolean {
        if (!isValidEmail(emailStr)) {
            mShowShortToast("이메일 형식을 확인해주세요")
            return false
        } else {
            if (passwordStr.isNullOrEmpty() || rePasswordStr.isNullOrEmpty()
                || !isValidPasswd(passwordStr) || !isValidPasswd(rePasswordStr) || passwordStr != rePasswordStr
            ) {
                mShowShortToast("비밀번호를 확인해주세요")
                return false
            } else {
                return true
            }
        }
    }

    private fun emailDuplicateCheck(){
        //이메일 중복 체크
        database.child("users").orderByChild("email").equalTo(emailStr)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val intent = Intent(applicationContext, ActivityJoin1::class.java)
                        intent.putExtra("email", emailStr)
                        intent.putExtra("password", passwordStr)
                        startActivity(intent)
                    } else {
                        mShowShortToast("이미 존재하는 이메일 계정이 있습니다.")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext,
                        databaseError.message,
                        Toast.LENGTH_SHORT).show()
                }
            })
    }


}