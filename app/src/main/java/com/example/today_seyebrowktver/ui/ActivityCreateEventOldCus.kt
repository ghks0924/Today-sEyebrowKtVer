

package com.example.today_seyebrowktver.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TextView
import com.example.today_seyebrowktver.data.EventData
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.ActivityCreateEventOldCusBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.util.*

class ActivityCreateEventOldCus : ActivityBase() {

    val REQUEST_LOAD_CUSTOMERS = 1111;

    //viewBinding
    private lateinit var binding: ActivityCreateEventOldCusBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid : String
    private lateinit var yearMonthForServer : String
    private lateinit var dateForServer : String
    var data = ArrayList<EventData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventOldCusBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setLayout()


    }


    private fun setLayout() {
        //uid 구하기
        uid = mAuth.currentUser!!.uid
        mChangeStatusBarColor("#ebbdc5")

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.loadCusData.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ActivityLoadCustomers::class.java)
            startActivityForResult(intent, REQUEST_LOAD_CUSTOMERS)
        })

        //예약 날짜 설정
        binding.dateContentTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.rsvDateLayout.setOnClickListener {
            val datePicker: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            val picker: MaterialDatePicker<*> = datePicker.build()
            picker.show(supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                var selectedDate = picker.headerText.toString()
                var year = picker.headerText.substring(0,4)
                var month:String = ""
                var day : String = ""

                Log.d("selectedDate", selectedDate)

//                if (selectedDate.indexOf("월") == 7 && selectedDate.indexOf("일") == 10){
//                    month = "0" + selectedDate.get(6)
//                    day = "0" + selectedDate.get(9)
//                } else if (selectedDate.indexOf("월") == 7 && selectedDate.indexOf("일") == 11){
//                    month = "0" + selectedDate.get(6)
//                    day = selectedDate.substring(9,11)
//                } else if(selectedDate.indexOf("월") == 8 && selectedDate.indexOf("일") == 11){
//                    month = selectedDate.substring(6,8)
//                    day = "0" + selectedDate.get(10).toString()
//                } else {
//                    month = selectedDate.substring(6,8)
//                    day = selectedDate.substring(10,12)
//                }

                yearMonthForServer = year+"-"+month
                dateForServer = year+month+day

                binding.dateContentTv.text = picker.headerText
                mShowLongToast(year+month+day)

                getEventsByDate()
            }

        }

        //예약 시간 설정
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

                var dspHour : String = ""
                when (hour.value){
                    6 -> dspHour = "06"
                    7 -> dspHour = "07"
                    8 -> dspHour = "08"
                    9 -> dspHour = "09"
                }

                var dspMin: String = ""
                when (minute.value) {
                    0 -> dspMin = "00분"
                    1 -> dspMin = "10분"
                    2 -> dspMin = "20분"
                    3 -> dspMin = "30분"
                    4 -> dspMin = "40분"
                    5 -> dspMin = "50분"

                }

                if (hour.value <10) {
                    binding.timeContentTv.text = dspHour + "시 " + dspMin
                } else {
                    binding.timeContentTv.text =
                        (hour.value).toString() + "시 " + dspMin
                }


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

            }
        }




    }

    //유효성 체크
    private fun isValidCheck() :Boolean {

        //고객 선택 강제
        if (binding.activityCreateEvtCusName.text.contains("선택")){
            mShowShortToast("고객을 선택해주세요")
            return false
        }

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


    fun getEventsByDate(){
        database.child("users").child(uid).child("events").child(yearMonthForServer).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newData: ArrayList<EventData> = ArrayList()
                for (ds in dataSnapshot.children) {
                    val event: EventData? = ds.getValue(EventData::class.java)
                    if (event != null) {
                        newData.add(event)
                    }
                }
                data.clear() //for문이 끝내기전까지 데이터를 유지하기 위해
                data.addAll(newData)

                Log.d("sizeOfData", data.size.toString() + "?")

            }


            //addListener sing은 한번만 불러오고
            //addValue는 데이터가 바꿀때마다 datachage 돈다.
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK){
            return
        }

        when (requestCode){
            REQUEST_LOAD_CUSTOMERS -> {
                binding.activityCreateEvtCusName.text = data!!.getStringExtra("cusName")
                binding.activityCreateEvtCusNbr.text = data!!.getStringExtra("cusNumber")
                binding.histroyCountTv.text = data!!.getIntExtra("numOfHistory",0).toString() + "회 방문"

                binding.activityCreateEvtCusName.setTextColor(Color.parseColor("#4f4f4f"))
                binding.activityCreateEvtCusNbr.setTextColor(Color.parseColor("#4f4f4f"))

            }
        }
    }

}




