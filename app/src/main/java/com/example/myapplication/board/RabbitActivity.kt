package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityRabbitBinding
import kotlinx.android.synthetic.main.activity_calendar2.*

class RabbitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRabbitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rabbit)

        //메모 저장
        binding.buttonBC2.setOnClickListener {
            val memoTitle = memotitle.text
            val memoText = memotext.text
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.rabbitRef
                .push()
                .setValue(boardModel(memoTitle.toString(), memoText.toString(), time, uid))

            Toast.makeText(this, "입력 완료", Toast.LENGTH_LONG).show()

            finish()
        }

        //뒤로가기 버튼
        binding.backbutton2.setOnClickListener {
            finish()
        }
    }
}