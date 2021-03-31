package com.example.today_seyebrowktver

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

open class ActivityBase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    //===================================Toast 띄우기===================================
    fun mShowShortToast(toast: String){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_SHORT).show()
    }

    fun mShowLongToast(toast: String){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }

    //==================================statusBar 색깔 바꾸기=============================
    fun mChangeStatusBarColor(colorStr: String){
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(colorStr)
        }
    }


    //================================키보드 관련 메서드===================================

    fun mKeyboardDown(){
        //키보드 내리기
        val immhide = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun mKeyboardUp(){
        // 키보드 띄우기
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }




}