package com.example.today_seyebrowktver.ui

import com.example.today_seyebrowktver.data.MemoData
import java.text.SimpleDateFormat
import java.util.*

class MemoViewModel {

    var memo: MemoData? = null
        set(memo) {
            field = memo
        }


    //각 리스트에 보여줄 항목들
    val title: String? //고객이름
        get() = memo?.memoTitle

    val content: String? //고객번호
        get() = memo?.memoContent

    // 현재시간을 msec 으로 구한다.
    val now = System.currentTimeMillis()
    val nowDate = Date(now)
    val sdfNow = SimpleDateFormat("yyyyMMddhhmmss")
    val nowYear = sdfNow.format(nowDate).substring(0, 4).toInt()
    val nowMonthDay = sdfNow.format(nowDate).substring(4, 8).toInt()

    //메모의 데이터 구하기
    val tmpDate: String?
        get() = memo?.memoDate
    val memoYear
    get() = memo?.memoDate?.substring(0, 4)?.toInt()
    val memoMonthDay : Int?
        get() = memo?.memoDate?.substring(4, 8)?.toInt()

    val date: String? //고객 방문 히스토리
        get() {
            if (nowYear == memoYear!!) { //현재 년도와 메모의 저장된 년도가 같으면
                if (nowMonthDay == memoMonthDay) { //월,일도 같으면
                    return tmpDate?.substring(8, 10) + "시 " + tmpDate?.substring(10, 12) + "분"
                } else { //다르면
                    return tmpDate?.substring(4, 6) + "월 " + tmpDate?.substring(6, 8) + "일"

                }
            } else { //다르면
                return tmpDate?.substring(0, 4) + "년 " + tmpDate?.substring(4,
                    6) + "월 " + tmpDate?.substring(6, 8) + "일"

            }
        }

}