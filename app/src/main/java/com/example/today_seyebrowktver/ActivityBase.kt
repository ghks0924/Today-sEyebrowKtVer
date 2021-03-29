package com.example.today_seyebrowktver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

open class ActivityBase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    //Toast 띄우기
    fun mShowShortToast(toast: String){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_SHORT).show()
    }

    fun mShowLongToast(toast: String){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }



}