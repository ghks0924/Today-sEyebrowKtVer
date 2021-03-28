package com.example.today_seyebrowktver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.today_seyebrowktver.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    //viewBinding
    private var binding: ActivityMainBinding? = null

    //Fragments
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentCustomers = FragmentCustomers()
        fragmentTransaction.add(R.id.frame_container, fragmentCustomers).commit()


    }

    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        binding = null
        super.onDestroy()
    }
}