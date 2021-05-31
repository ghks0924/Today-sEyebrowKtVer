package com.example.today_seyebrowktver

import android.app.Dialog
import android.content.Context
import android.view.Window


class ProgressDialog(context : Context) {
    private val dlg = Dialog(context)

    fun start(context: Context){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_progressbar)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        dlg.show()
    }
}