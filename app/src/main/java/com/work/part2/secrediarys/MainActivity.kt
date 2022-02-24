package com.work.part2.secrediarys


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaCodec.MetricsConstants.MODE
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    //lazy하게 초기화 하는 이유 MainActivity가 생성될 시점에는 view가
    //그려지지 않는 시점이기 때문에
    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton by lazy {
        findViewById<Button>(R.id.openButton)
    }

    private val changePasswordButton by lazy {
        findViewById<Button>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3
        //lazy가 init하는 시점은 해당 객체가 사용될 때이다.
        //numberPicker은 직접적으로 사용하지는 않으므로 먼저 호출

        openButton.setOnClickListener {

            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 모드입니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            //해당 app에서만 사용하는 걸 원하기 때문에 MODE_PRIVATE 로 선언해준다.

            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            //(key value , default value)
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                //패스워드 일치 성공
                //다이어리 화면으로 이동


                startActivity(Intent(this,DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
                //패스워드 불었치 실패
            }
        }

        //패스워드 변경 기능
        changePasswordButton.setOnClickListener {
            //눌렀을 떄 다른 행동을 하지 않도록 예외 처리를 해야한다.
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (changePasswordMode) {
                //번호를 저장하는 기능

                passwordPreferences.edit(true) {

                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)


            } else {
                //changePasswordMode 가 활성화하고 현재 입력한 패스워드가 저장한 패스워드가 맞느지 확인

                //해당 app에서만 사용하는 걸 원하기 때문에 MODE_PRIVATE 로 선언해준다.

                //(key value , default value)
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    //패스워드가 일치하여 패스워드 변경모드로 전환

                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_LONG).show()

                    changePasswordButton.setBackgroundColor(Color.RED)


                    //startActivity()
                } else {
                    showErrorAlertDialog()
                    //패스워드 불었치 실패
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}