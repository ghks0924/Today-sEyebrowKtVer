package com.example.today_seyebrowktver

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class DialogEachEvent(context: Context) {

    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private lateinit var backIv : ImageView
    private lateinit var editIv : ImageView
    private lateinit var rsvDateLayout : ConstraintLayout
    private lateinit var rsvMenuLayout: ConstraintLayout
    private lateinit var newRb: RadioButton
    private lateinit var oldRb: RadioButton
    private lateinit var memoEt: EditText

    private lateinit var noshowBtn: MaterialButton
    private lateinit var completeBtn: MaterialButton
    private lateinit var cameraIv: ImageView

    fun start(context: Context){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_each_event)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(true)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함



        dlg.show()
    }



}