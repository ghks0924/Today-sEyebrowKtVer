package com.example.today_seyebrowktver

import android.app.AlertDialog
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TextView
import com.example.today_seyebrowktver.databinding.ActivityCreateEventNewCusBinding
import com.google.android.material.datepicker.MaterialDatePicker

class ActivityCreateEventNewCus : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateEventNewCusBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventNewCusBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setLayout()
    }

    private fun setLayout() {
        val intent = intent
        val customerName = intent.getStringExtra("name")
        val customerNumber = intent.getStringExtra("number")

        binding.activityCreateEvtCusName.text = customerName
        binding.activityCreateEvtCusNbr.text = customerNumber


        mChangeStatusBarColor("#ebbdc5")

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.dateContentTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.rsvDateLayout.setOnClickListener {
            val datePicker: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            val picker: MaterialDatePicker<*> = datePicker.build()
            picker.show(supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                val datetetete =
                    picker.headerText.replace("년 ", "-").replace("월 ", "-").replace("일", "")
                binding.dateContentTv.text = picker.headerText
                mShowLongToast(datetetete)
            }

        }

        binding.timeContentTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.rsvTimeLayout.setOnClickListener {


            val dialog = AlertDialog.Builder(this).create()
            val edialog: LayoutInflater = LayoutInflater.from(this)
            val mView: View = edialog.inflate(R.layout.dialog_custom_timepicker, null)

            val hour: NumberPicker = mView.findViewById(R.id.hour_picker)
            val minute: NumberPicker = mView.findViewById(R.id.min_picker)
            val cancel: TextView = mView.findViewById(R.id.cancel_btn)
            val ok: TextView = mView.findViewById(R.id.ok_btn)

            //  순환 안되게 막기
            hour.wrapSelectorWheel = false
            minute.wrapSelectorWheel = false

            //  editText 설정 해제
            hour.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            minute.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            //  최소값 설정
            hour.minValue = 6
            minute.minValue = 0

            //  최대값 설정
            hour.maxValue = 23
            minute.maxValue = 5


            //보여지는 값 세팅 ; output은 다름
            hour.displayedValues = arrayOf("06시",
                "07시",
                "08시",
                "09시",
                "10시",
                "11시",
                "12시",
                "13시",
                "14시",
                "15시",
                "16시",
                "17시",
                "18시",
                "19시",
                "20시",
                "21시",
                "22시",
                "23시")

            minute.displayedValues = arrayOf("00분", "10분", "20분", "30분", "40분", "50분")

            minute.setOnValueChangedListener { picker, oldVal, newVal ->

            }

            //초기값 세팅
            hour.value = 9
            minute.value = 0


            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            //  완료 버튼 클릭 시
            ok.setOnClickListener {

                var dspMin: String = ""
                when (minute.value) {
                    0 -> dspMin = "00분"
                    1 -> dspMin = "10분"
                    2 -> dspMin = "20분"
                    3 -> dspMin = "30분"
                    4 -> dspMin = "40분"
                    5 -> dspMin = "50분"

                }

                binding.timeContentTv.text =
                    (hour.value).toString() + "시 " + dspMin

                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()
            dialog.show()

            //크기조정
            val dm = applicationContext.resources.displayMetrics
            val width = (dm.widthPixels * 0.7).toInt() // Display 사이즈의 90%
            val params: ViewGroup.LayoutParams = dialog.getWindow()!!.getAttributes()
            params.width = width
            dialog.getWindow()!!.setAttributes(params as WindowManager.LayoutParams)

        }

        binding.okBtn.setOnClickListener {
            if (isValidCheck()){
                //예약생성
//                finish()
            }
        }
    }

    private fun isValidCheck() :Boolean {
        //예약 유형 선택 강제
        if(!binding.newRb.isChecked && !binding.retouchRb.isChecked){
            mShowShortToast("예약 유형을 선택해주세요")
            return false
        }

        //예약 날짜 선택 장제
        if (binding.dateContentTv.text.contains("선택")){
            mShowShortToast("예약 날짜를 선택해주세요")
            return false
        }

        //예약 시간 선택 강제
        if (binding.timeContentTv.text.contains("입력")){
            mShowShortToast("예약 시간을 선택해주세요")
            return false
        }

        //예약 중복 막기
//        if ()


            return true
    }

}