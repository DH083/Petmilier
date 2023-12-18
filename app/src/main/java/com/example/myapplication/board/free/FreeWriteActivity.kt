package com.example.myapplication.board.free

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityFreeWriteBinding
import com.example.myapplication.memoModel
import kotlinx.android.synthetic.main.activity_calendar2.*

class FreeWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFreeWriteBinding
    private lateinit var key: String
    private val Tag = FreeEditActivity::class.java
    private lateinit var writerUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_free_write)

// 메모 저장

        binding.buttonBC2.setOnClickListener {
            val memoTitle = memotitle.text
            val memoText = memotext.text
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.freeRef
                .push()
                .setValue(boardModel(memoTitle.toString(), memoText.toString(),time ,uid))

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