package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.FragmentSalesBinding
import com.example.today_seyebrowktver.viewmodel.FragmentMemoViewModel
import com.example.today_seyebrowktver.viewmodel.FragmentSalesViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.slider.LabelFormatter

private val TAG = "FragmentSales"

class FragmentSales : Fragment() {
    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    private var NoOfEmp = mutableListOf<BarEntry>()
    private var xAxisValue = ArrayList<String>()

    //viewModel
    private val fragmentSalesViewModel: FragmentSalesViewModel by lazy {
        ViewModelProvider(this).get(FragmentSalesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d(TAG, "onViewCreated")
        binding.previousMonthImage.setOnClickListener {

        }

        binding.fragmentSalesMonthlySalesTv.setOnClickListener {
            val intent = Intent(context, ActivityDetailedPerformance::class.java)
            startActivity(intent)
        }
        binding.nextMonthImage.setOnClickListener {

        }

        binding.calendarIv.setOnClickListener {
            val intent = Intent(context, ActivityMonthlyPerformance::class.java)
            startActivity(intent)
        }

        mTestDataSet()
        mTestDataSet2()

    }

    private fun mTestDataSet() {
        //barchart
        val entryList = mutableListOf<BarEntry>()
        entryList.add(BarEntry(0f, 200000f))
        entryList.add(BarEntry(1f, 1000000f))

        val labels = arrayOf("현금", "카드")

        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97),
            Color.rgb(255, 140, 157)
        )

        val barDataSet = BarDataSet(entryList, "No Of Employee")
            .apply {
                setDrawIcons(false)
                setDrawValues(true)
                valueTextSize = 12f
                valueTextColor = Color.parseColor("#221f20")
                valueTypeface = Typeface.DEFAULT_BOLD
                colors = colorList // bar 색상 여러가지
                valueFormatter = PaymentCustomFormatter()
            }

        val data = BarData(barDataSet).apply {
            barWidth = 0.35f
        }


        val barChart = binding.fragmentSalesCashcardBarchart
        barChart.data = data

        barChart.apply {
            setTouchEnabled(false) //차트 터치 막기
            //터치, Pinch 상호작용
            setScaleEnabled(false)
            setPinchZoom(false)
            animateXY(0, 3000)  //Chart가 그려질때 애니메이션
            description.isEnabled = false    //Chart 밑에 description 표시 유무

            legend.isEnabled = false  //Legend는 차트의 범례를 의미합니다
            //범례가 표시될 위치를 설정
//            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT



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
                valueFormatter = CustomerCustomFormatter()
            }


        val data = BarData(barDataSet)
        data.barWidth = 0.45f

        val barChart = binding.fragmentSalesCustomerBarchart
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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
    }

    companion object {
        fun newInstance(): FragmentSales {
            return FragmentSales()
        }
    }

    //그래프 위에 가격을 원단위로 나타내기 위해
    private inner class PaymentCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val score = value.toString().split(".")
            return score[0]+" 원"
        }
    }

    //그래프 위에 가격을 원단위로 나타내기 위해
    private inner class CustomerCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val score = value.toString().split(".")
            return score[0]+" 명"
        }
    }

    //label 커스텀해서 쓰려면 쓰자
    class LabelCustomFormatter : ValueFormatter() {
        private var index = 0

        override fun getFormattedValue(value: Float): String {
            index = value.toInt()
            return when (index) {
                6 -> "DR"
                7 -> "WU"
                8 -> "IRON"
                9 -> "S/W"
                10 -> "PUTT"
                else -> throw IndexOutOfBoundsException("index out")
            }
        }

        override fun getBarStackedLabel(value: Float, stackedEntry: BarEntry?): String {
            return super.getBarStackedLabel(value, stackedEntry)
        }
    }
}