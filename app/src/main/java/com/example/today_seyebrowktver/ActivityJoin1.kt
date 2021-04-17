package com.example.today_seyebrowktver

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.view.View
import android.widget.CompoundButton
import com.example.today_seyebrowktver.databinding.ActivityJoin1Binding
import java.text.SimpleDateFormat
import java.util.*

class ActivityJoin1 : ActivityBase(), View.OnClickListener {

    //viewBinding
    private lateinit var binding: ActivityJoin1Binding

    //data from prev Activity
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoin1Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        getDataFromPrevAct() //이전액티비티에서 넘긴 데이터 받기
        setLayout()
    }

    private fun getDataFromPrevAct() {
        val intent2 = intent
        email = intent2.getStringExtra("email")
        password = intent2.getStringExtra("password")
    }

    private fun setLayout() {
        //click event
        binding.backCardview.setOnClickListener(this)
        binding.nextTv.setOnClickListener(this)
        binding.regionTv.setOnClickListener(this)

        binding.serviceAgree.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.privacyAgree.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.fullAgree.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.serviceAgree.isChecked = true
                binding.privacyAgree.isChecked = true
            } else {
                binding.serviceAgree.isChecked = false
                binding.privacyAgree.isChecked = false
            }


        })

        binding.phoneNbEt.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        val phoneNumber = getPhoneNumber()
        binding.phoneNbEt.setText(phoneNumber)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backCardview -> finish()

            binding.nextTv -> {
                if (isValid()) {
                    mShowShortToast("아몰랑")
                } else {
                    mShowShortToast("안되는데용")
                }
            }

            binding.regionTv -> {

            }
        }
    }

    //휴대전화 번호 가져오기
    @SuppressLint("MissingPermission")
    fun getPhoneNumber(): String {
        val telephony = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        var phoneNumber = ""
        try {
            if (telephony.line1Number != null) {
                phoneNumber = telephony.line1Number
            } else {
                if (telephony.simSerialNumber != null) {
                    phoneNumber = telephony.simSerialNumber
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (phoneNumber.startsWith("+82")) {
            phoneNumber = phoneNumber.replace("+82", "0") // +8210xxxxyyyy 로 시작되는 번호
        }
        //phoneNumber = phoneNumber.substring(phoneNumber.length()-10,phoneNumber.length());
        //phoneNumber="0"+phoneNumber;
        phoneNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country)
        } else {
            PhoneNumberUtils.formatNumber(phoneNumber)
        }
        return phoneNumber
    }

    //입력 잘했는지 확인하는 메서드(빈칸 X)
    fun isValid(): Boolean {
        if (!checkTermAgree()) {
            mShowShortToast("약관에 동의해주세요")
            return false
        }
        var numberStr = binding.phoneNbEt.text.toString().trim()
        if (!isValidCellPhoneNumber(numberStr)) {
            mShowShortToast("휴대폰번호를 확인해주세요")
            return false
        }
        var refinedNumberStr = numberStr.replace("-", "")
        val shopNm: String = binding.shopNmEt.text.toString().trim { it <= ' ' }
        if (shopNm.isEmpty()) {
            mShowShortToast("가게명을 입력해주세요")
            return false
        }
        val regionStr: String = binding.regionTv.getText().toString().trim { it <= ' ' }
        if (regionStr.isEmpty()) {
            mShowShortToast("지역을 선택해주세요.")
            return false
        }
        val birth: String = binding.birthEt.getText().toString().trim { it <= ' ' }
        if (!isVaildBirth(birth)) {
            return false
        }
        if (!binding.maleRb.isChecked() && !binding.femaleRb.isChecked()) {
            mShowShortToast("성별을 선택해주세요.")
            return false
        }
        return true
    }

    //약관동의 확인 메서드
    fun checkTermAgree(): Boolean {
        if (binding.fullAgree.isChecked || (binding.serviceAgree.isChecked && binding.privacyAgree.isChecked)) {
            return true
        } else {
            return false
        }
    }

    //생년월일유효성검사
    private fun isVaildBirth(birth: String): Boolean {

        // 현재시간을 msec 으로 구한다.
        val now = System.currentTimeMillis()
        // 현재시간을 date 변수에 저장한다.
        val date = Date(now)
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        val sdfNow = SimpleDateFormat("yyyy")
        // nowDate 변수에 값을 저장한다.
        val nowDate = sdfNow.format(date)
        val year = birth.substring(0, 4)
        val month = birth.substring(4, 6)
        val day = birth.substring(6)
        val yearInt = year.toInt()
        val monthInt = month.toInt()
        val dayaInt = day.toInt()
        val yearNow = nowDate.toInt()
        return if (birth.length != 8) {
            mShowShortToast("생년월일을 확인해주세요")
            false
        } else {
            if (1900 > yearInt || yearInt > yearNow) {
                mShowShortToast("생년월일을 확인해주세요")
                false
            } else if (monthInt < 1 || monthInt > 12) {
                mShowShortToast("생년월일을 확인해주세요")
                false
            } else if (dayaInt < 1 || dayaInt > 31) {
                mShowShortToast("생년월일을 확인해주세요")
                false
            } else if ((monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11) && dayaInt == 31) {
                mShowShortToast("생년월일을 확인해주세요")
                false
            } else if (monthInt == 2) {
                val isLeap = yearInt % 4 == 0 && (yearInt % 100 != 0 || yearInt % 400 == 0)
                if (dayaInt > 29 || dayaInt == 29 && !isLeap) {
                    mShowShortToast("생년월일을 확인해주세요")
                    false
                } else {
                    true
                } //end of if (day>29 || (day==29 && !isleap))
            } else {
                true
            }
        }
    }
}