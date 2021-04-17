package com.example.today_seyebrowktver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.example.today_seyebrowktver.databinding.ActivityJoin0Binding
import java.util.regex.Pattern

class ActivityJoin0 : ActivityBase(), View.OnClickListener {

    //viewBinding
    private lateinit var binding: ActivityJoin0Binding

    private lateinit var emailStr: String
    private lateinit var passwordStr: String
    private lateinit var rePasswordStr: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoin0Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {
        binding.backCardview.setOnClickListener(this)
        binding.nextTv.setOnClickListener(this)

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
            binding.backCardview -> {
                finish()
            }

            binding.nextTv -> {
                if (isValidToNext()) {
                    val intent = Intent(this, ActivityJoin1::class.java)
                    intent.putExtra("email", emailStr)
                    intent.putExtra("password", passwordStr)
                    startActivity(intent)
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


}