package com.example.today_seyebrowktver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityLoading : ActivityBase() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val progressDlg = ProgressDialog(this)
        progressDlg.start(this)

        if (mAuth.currentUser != null){
            val intent = Intent(applicationContext, ActivityMain::class.java)
            startActivity(intent)
            this.finish()
        } else{
            val intent = Intent(applicationContext, ActivityLogin::class.java)
            startActivity(intent)
            this.finish()
        }

    }

}