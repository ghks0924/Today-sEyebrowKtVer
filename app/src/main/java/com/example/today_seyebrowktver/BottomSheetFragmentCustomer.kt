package com.example.today_seyebrowktver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton


class BottomSheetFragmentCustomer : BottomSheetDialogFragment() {

    private val mContext: Context? = context



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.bottomsheet_create_cus, container, false)
        val manualCreateBtn: MaterialButton = view.findViewById(R.id.manuallyAdd)
        val fromBookBtn: MaterialButton = view.findViewById(R.id.fromPhonebook)

        manualCreateBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateCustomer::class.java)
            intent.putExtra("origin", "createCustomer")
            startActivity(intent)
            dismiss()
        })

        fromBookBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateCustomerFromBook::class.java)
            startActivity(intent)
            dismiss()
        })

        return view
    }

    override fun getTheme(): Int = R.style.TopRoundedBottomSheetDialogTheme

}