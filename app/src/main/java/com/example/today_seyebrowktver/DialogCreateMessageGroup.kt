package com.example.today_seyebrowktver

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.currentCoroutineContext

class DialogCreateMessageGroup(context: Context) {
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var lblDesc : TextView
    private lateinit var edittext : EditText
    private lateinit var btnOK : MaterialButton
    private lateinit var btnCancel : MaterialButton
    private lateinit var listener : MyDialogOKClickedListener
    private lateinit var cancelListener : MyCancelClickedListener

    fun start(){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_create_messagegroup)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(true)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        lblDesc = dlg.findViewById(R.id.dialog_tv)
        edittext = dlg.findViewById(R.id.dialog_edittext)

        btnOK = dlg.findViewById(R.id.dialog_ok_btn)
        btnOK.setOnClickListener {

            listener.onOKClicked("확인을 눌렀습니다")

            dlg.dismiss()
        }

        btnCancel = dlg.findViewById(R.id.dialog_cancel_btn)
        btnCancel.setOnClickListener {

            cancelListener.onCancelClicked("취소")
            dlg.dismiss()
        }

        edittext.requestFocus()

        dlg.show()
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
        fun onOKClicked(content : String)

    }

    interface MyCancelClickedListener {
        fun onCancelClicked(content: String)
    }
}