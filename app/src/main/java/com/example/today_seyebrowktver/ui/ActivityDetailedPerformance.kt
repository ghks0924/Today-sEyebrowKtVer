package com.example.today_seyebrowktver.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.ActivityDetailedPerformanceBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class ActivityDetailedPerformance : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityDetailedPerformanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedPerformanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {
        //previous iv click
        binding.previousMonthIv.setOnClickListener {

        }

        //date tv click
        binding.actDetailedPerformMonthTv.setOnClickListener {
            val dialog = AlertDialog.Builder(this@ActivityDetailedPerformance).create()
            val edialog: LayoutInflater = LayoutInflater.from(this@ActivityDetailedPerformance)
            val mView: View = edialog.inflate(R.layout.dialog_custom_datepicker, null)

            val year: NumberPicker = mView.findViewById(R.id.year_picker)
            val month: NumberPicker = mView.findViewById(R.id.month_picker)
            val cancel: TextView = mView.findViewById(R.id.cancel_btn)
            val ok: TextView = mView.findViewById(R.id.ok_btn)

            //  순환 안되게 막기
            year.wrapSelectorWheel = false
            month.wrapSelectorWheel = false

            //  editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            //  최소값 설정
            year.minValue = 2015
            month.minValue = 1


//             현재시간을 msec 으로 구한다.
            val now = System.currentTimeMillis()
            // 현재시간을 date 변수에 저장한다.
            val date = Date(now)
            // 시간을 나타냇 포맷을 정한다 ( yyyy
            val formatYear = SimpleDateFormat("yyyy")
            val formatMonth = SimpleDateFormat("MM")
            // nowDate 변수에 값을 저장한다.
            val formatDateYear = formatYear.format(date)
            val formatDateMonth = formatMonth.format(date)
            val maxYearInt = Integer.parseInt(formatDateYear) + 5

            //  최대값 설정
            year.maxValue = maxYearInt //올해 + 5
            month.maxValue = 12


            //보여지는 값 세팅 ; output은 다름
//            hour.displayedValues = arrayOf("06시")

            month.displayedValues =
                arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")

            month.setOnValueChangedListener { picker, oldVal, newVal ->
            }

            //초기값 세팅
            year.value = Integer.parseInt(formatDateYear) //현재 년도
            month.value = Integer.parseInt(formatDateMonth)

            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            //  완료 버튼 클릭 시
            ok.setOnClickListener {

                Toast.makeText(this@ActivityDetailedPerformance,
                    year.value.toString() + "년" + month.value + "월",
                    Toast.LENGTH_SHORT).show()

//                var dspHour: String = ""
//                when (hour.value) {
//                    6 -> dspHour = "06"
//                    7 -> dspHour = "07"
//                    8 -> dspHour = "08"
//                    9 -> dspHour = "09"
//                }
//
//                var dspMin: String = ""
//                when (minute.value) {
//                    0 -> dspMin = "00분"
//                    1 -> dspMin = "10분"
//                    2 -> dspMin = "20분"
//                    3 -> dspMin = "30분"
//                    4 -> dspMin = "40분"
//                    5 -> dspMin = "50분"
//
//                }
//
//                if (hour.value < 10) {
//                    binding.timeContentTv.text = dspHour + "시 " + dspMin
//                } else {
//                    binding.timeContentTv.text =
//                        (hour.value).toString() + "시 " + dspMin
//                }


                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()
            dialog.show()

            //크기조정
            val dm = resources.displayMetrics
            val width = (dm.widthPixels * 0.7).toInt() // Display 사이즈의 90%
            val params: ViewGroup.LayoutParams = dialog.getWindow()!!.getAttributes()
            params.width = width
            dialog.getWindow()!!.setAttributes(params as WindowManager.LayoutParams)
        }

        //more icon
        binding.moreIcon.setOnClickListener {

        }

        //next month iv click
        binding.nextMonthImage.setOnClickListener {

        }

        mTestDataSet2()
        mTestDataSet3()
    }

    private fun mTestDataSet2() {
        //barchart

        val entryList = mutableListOf<BarEntry>()
        entryList.add(BarEntry(0f, 10f))
        entryList.add(BarEntry(1f, 5f))
        entryList.add(BarEntry(2f, 7f))

        val labels = arrayOf("신규고객", "기존고객", "리터치")

        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97)
        )

        val barDataSet = BarDataSet(entryList, "entryList")
            .apply {
                setDrawIcons(false)
                setDrawValues(true)
                valueTextSize = 12f
                valueTextColor = Color.parseColor("#221f20")
                valueTypeface = Typeface.DEFAULT_BOLD
                colors = colorList // bar 색상 여러가지
//                color = ColorTemplate.rgb("#ff7b22") // bar 단일 color
                valueFormatter = PaymentCustomFormatter()
            }


        val data = BarData(barDataSet)
        data.barWidth = 0.45f

        val barChart = binding.actDetailedPerformBycusBarchart
        barChart.data = data

        barChart.apply {
            //터치, Pinch 상호작용
            setTouchEnabled(false) //차트 터치 막기
            setScaleEnabled(false)
            setPinchZoom(false)
            animateXY(0, 3000)  //Chart가 그려질때 애니메이션
            description.isEnabled = false //Chart 밑에 description 표시 유무

            //Legend는 차트의 범례를 의미합니다
            //범례가 표시될 위치를 설정
//            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            legend.isEnabled = false //차트 범례 설정

            axisLeft.run {
                axisMinimum = 0f
                setDrawLabels(false) //좌측 측면에도 격자 없애기
                setDrawGridLines(false) //grid없앰
                setDrawAxisLine(false) //축 없앰
            }

            axisRight.run {
                axisMinimum = 0f  // 최소값  //차트 제일 밑이 0부터 시작하고 싶은 경우 설정합니다.
                setDrawLabels(false)   //기본적으로 차트 우측 축에도 데이터가 표시됩니다 //이를 활성화/비활성화 하기 위함
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }


            xAxis.run {
                //xAxis, yAxis 둘다 존재하여 따로 설정이 가능합니다
                //차트 내부에 Grid 표시 유무
                setDrawGridLines(false)
                setDrawAxisLine(false)

                //x축 데이터 표시 위치
                position = XAxis.XAxisPosition.BOTTOM
                //x축 데이터 갯수 설정
                labelCount = entryList.size

                granularity = 1f // 간격설정
                textSize = 11f

                typeface = Typeface.DEFAULT_BOLD
                textColor = Color.parseColor("#221f20")
                //x축 이름에 float아닌 원하는 String으로 넣어주기 위해서
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt()]
                    }
                }
            }

            invalidate()
        }


    }

    private fun mTestDataSet3() {
        //barchart

        val entryList = mutableListOf<BarEntry>()
        entryList.add(BarEntry(0f, 10f))
        entryList.add(BarEntry(1f, 5f))
        entryList.add(BarEntry(2f, 7f))

        val labels = arrayOf("신규고객", "기존고객", "리터치")

        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97)
        )

        val barDataSet = BarDataSet(entryList, "entryList")
            .apply {
                setDrawIcons(false)
                setDrawValues(true)
                valueTextSize = 12f
                valueTextColor = Color.parseColor("#221f20")
                valueTypeface = Typeface.DEFAULT_BOLD
                colors = colorList // bar 색상 여러가지
//                color = ColorTemplate.rgb("#ff7b22") // bar 단일 color
                valueFormatter = PaymentCustomFormatter()
            }


        val data = BarData(barDataSet)
        data.barWidth = 0.45f

        val barChart = binding.actDetailedPerformBymenuBarchart
        barChart.data = data

        barChart.apply {
            //터치, Pinch 상호작용
            setTouchEnabled(false) //차트 터치 막기
            setScaleEnabled(false)
            setPinchZoom(false)
            animateXY(0, 3000)  //Chart가 그려질때 애니메이션
            description.isEnabled = false //Chart 밑에 description 표시 유무

            //Legend는 차트의 범례를 의미합니다
            //범례가 표시될 위치를 설정
//            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            legend.isEnabled = false //차트 범례 설정

            axisLeft.run {
                axisMinimum = 0f
                setDrawLabels(false) //좌측 측면에도 격자 없애기
                setDrawGridLines(false) //grid없앰
                setDrawAxisLine(false) //축 없앰
            }

            axisRight.run {
                axisMinimum = 0f  // 최소값  //차트 제일 밑이 0부터 시작하고 싶은 경우 설정합니다.
                setDrawLabels(false)   //기본적으로 차트 우측 축에도 데이터가 표시됩니다 //이를 활성화/비활성화 하기 위함
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }


            xAxis.run {
                //xAxis, yAxis 둘다 존재하여 따로 설정이 가능합니다
                //차트 내부에 Grid 표시 유무
                setDrawGridLines(false)
                setDrawAxisLine(false)

                //x축 데이터 표시 위치
                position = XAxis.XAxisPosition.BOTTOM
                //x축 데이터 갯수 설정
                labelCount = entryList.size

                granularity = 1f // 간격설정
                textSize = 11f

                typeface = Typeface.DEFAULT_BOLD
                textColor = Color.parseColor("#221f20")
                //x축 이름에 float아닌 원하는 String으로 넣어주기 위해서
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt()]
                    }
                }
            }

            invalidate()
        }


    }

    //그래프 위에 가격을 원단위로 나타내기 위해
    private inner class PaymentCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val score = value.toString().split(".")
            return score[0]+" 원"
        }
    }
}