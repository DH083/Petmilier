package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityCalendar2Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_calendar2.*
import java.sql.Timestamp
import java.time.LocalDateTime.now
import java.util.*

class Calendar2Activity : AppCompatActivity() {
    lateinit var dateEdit: EditText

    private lateinit var binding: ActivityCalendar2Binding

    val fbdb = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar2)


// 메모 저장

        binding.buttonBC2.setOnClickListener {
            val memoTitle = memotitle.text
            val memoText = memotext.text
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.memoRef
                .push()
                .setValue(memoModel(memoTitle.toString(), memoText.toString(),time ,uid))

            Toast.makeText(this, "입력 완료", Toast.LENGTH_LONG).show()

            finish()
        }


// 뒤로가기 버튼
        val backbutton2 = findViewById<Button>(R.id.backbutton2)

        backbutton2.setOnClickListener {
            finish()
        }
    }
}