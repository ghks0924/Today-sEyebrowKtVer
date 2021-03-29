package com.example.today_seyebrowktver

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            (activity as BaseActivity).mShowLongToast("new CUs")
        })

        oldCusBtn.setOnClickListener(View.OnClickListener {
            (activity as BaseActivity).mShowLongToast("old CUs")
        })

        skipCusBtn.setOnClickListener(View.OnClickListener {
            (activity as BaseActivity).mShowLongToast("skip CUs")
        })

        return view
    }
}