package com.example.today_seyebrowktver

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.regex.Pattern


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
    fun mShowShortToast(toast: String) {
        Toast.makeText(applicationContext, toast, Toast.LENGTH_SHORT).show()
    }

    fun mShowLongToast(toast: String) {
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }

    //==================================statusBar 색깔 바꾸기=============================
    fun mChangeStatusBarColor(colorStr: String) {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(colorStr)
        }
    }


    //================================키보드 관련 메서드===================================

    fun mKeyboardDown() {
        //키보드 내리기
        val immhide = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun mKeyboardUp() {
        // 키보드 띄우기
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    //========================전화번호 유효검사=========================
    fun isValidCellPhoneNumber(cellphoneNumber: String?): Boolean {
        var cellphoneNumber = cellphoneNumber
        var returnValue = false
        return try {
            val regex = "^\\s*(010|011|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$"
            val p = Pattern.compile(regex)
            val m = p.matcher(cellphoneNumber)
            if (m.matches()) {
                returnValue = true
            }
            if (returnValue && cellphoneNumber != null && cellphoneNumber.length > 0 && cellphoneNumber.startsWith(
                    "010"
                )
            ) {
                cellphoneNumber = cellphoneNumber.replace("-", "")
                if (cellphoneNumber.length != 11) {
                    returnValue = false
                }
            }
            returnValue
        } catch (e: Exception) {
            false
        }
    }

    //====권한설정 TedPermission===
    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            mShowShortToast("권한 설정 완료")
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            mShowShortToast("권한 거부")
        }
    }

    fun setPermittionCheck() {
        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.READ_PHONE_STATE
            )
            .check();
    }

    //액티비티 상태바 수정하기
    fun setStatusBarColor(activity : Activity, color : Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }




}