package com.example.today_seyebrowktver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragmentCheckSave : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_check_if_save, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cancelTv:TextView = view.findViewById(R.id.cancel_tv)
        val donotSaveTv:TextView = view.findViewById(R.id.donotsave_tv)
        val saveTv:TextView = view.findViewById(R.id.save_tv)

        cancelTv.setOnClickListener {
            dismiss()
        }

        donotSaveTv.setOnClickListener {
            dismiss()
            requireActivity().finish()
        }
        saveTv.setOnClickListener {
            dismiss()
            (activity as ActivityEditCustomer).saveUpdatedCustomerData()
        }
    }

    override fun getTheme(): Int = R.style.AllRoundedBottomSheetDialogTheme

}