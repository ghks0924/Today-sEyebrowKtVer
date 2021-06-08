package com.example.today_seyebrowktver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.today_seyebrowktver.ui.ActivityCreateCustomer
import com.example.today_seyebrowktver.ui.ActivityCreateEventOldCus
import com.example.today_seyebrowktver.ui.ActivityCreateEventSkipCus
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class BottomSheetFragmentEvent : BottomSheetDialogFragment(){

    private val mContext: Context? = context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.bottomsheet_create_evt, container, false)
        val newCusBtn: MaterialButton = view.findViewById(R.id.new_cus)
        val oldCusBtn: MaterialButton = view.findViewById(R.id.old_cus)
        val skipCusBtn: MaterialButton = view.findViewById(R.id.skip_cus)

        newCusBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateCustomer::class.java)
            intent.putExtra("origin", "createEvent")
            startActivity(intent)
            dismiss()
        })

        oldCusBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateEventOldCus::class.java)
            startActivity(intent)
            dismiss()
        })

        skipCusBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateEventSkipCus::class.java)
            startActivity(intent)
            dismiss()
        })

        return view
    }

    override fun getTheme(): Int = R.style.TopRoundedBottomSheetDialogTheme
}