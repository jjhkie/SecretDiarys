package com.work.part2.secrediarys

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {

    //handler 에는 looper라는 것을 안쪽에 넣어줘야 한다.
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
        //그 후 manifest에 activity를 추가해줘야 한다.
        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))

        //#1
        //작성하다가 잠시 멈췄을 때 저장하는 코드
        //쓰레드 기능을 사용해보자
        //쓰레드에 넣을 수 있는 runnable 을 사용
        val runnable = Runnable {
            detailPreferences.edit {
                putString("detail", diaryEditText.text.toString())
            }
            Log.d("Detail","SAVE ${diaryEditText.text.toString()}")
        }

        //#2
        //작성할 때마다 저장하는 코드
//        diaryEditText.addTextChangedListener {
//            detailPreferences.edit {
//                putString("detail",diaryEditText.text.toString())
//            }
//        }

        diaryEditText.addTextChangedListener {
            Log.d("Detail","TextChanged:: $it")
            handler.removeCallbacks(runnable)//아직 실행되지 않고 팬딩(보류)된 runnable이 존재할 경우 지워준다.
            handler.postDelayed(runnable, 500)//0.5초
        }

        //핸들러
        //쓰레드를 열었을 때 ui에서 처리하는 쓰레드를 ui 쓰레드
        //새로운 쓰레드를 열었을 떄는 ui 쓰레드가 아니다.
        //하지만 관리를 할 때 생성한 쓰레드와 ui 쓰레드를 연결해줘야할 필요가 있다.
        //메인 쓰레드가 아닐 때는 ui 체인지 동작을 수행할 수 없기 때문이다.
    }
}


