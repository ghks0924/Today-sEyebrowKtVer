package com.example.today_seyebrowktver

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class DialogShowMessageGroup(context: Context) {

    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private lateinit var addCardview : MaterialCardView
    private lateinit var rv : RecyclerView
    private var adapter: RvMessageGroupAdapter? = null

    private lateinit var listener : MyDialogOKClickedListener
    private lateinit var cancelListener : MyCancelClickedListener

    fun start(context: Context, data: ArrayList<MessageGroupData>){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_show_messagegroup)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(true)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        rv = dlg.findViewById(R.id.rv)
        addCardview = dlg.findViewById(R.id.plus_cardview)

        adapter = RvMessageGroupAdapter(data)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter

        adapter!!.itemClick = object :RvMessageGroupAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(context, data[position].groupName, Toast.LENGTH_SHORT).show()
                dlg.dismiss()

            }
        }

        addCardview.setOnClickListener {

            dlg.dismiss()
        }


        dlg.setOnDismissListener {
            val immhide = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }

        dlg.show()
    }

    private fun setData() {

    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }

    fun setOnCanCelClickedListener(listener: (String) -> Unit){
        this.cancelListener = object :MyCancelClickedListener{
            override fun onCancelClicked(content: String) {
                listener(content)
            }

        }
    }



    interface MyDialogOKClickedListener {
        fun onOKClicked(content: String)

    }

    interface MyCancelClickedListener {
        fun onCancelClicked(content: String)
    }
}