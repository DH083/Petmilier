package com.example.myapplication.board.recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityRecBinding

class RecActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rec)

        binding.backbutton2.setOnClickListener {
            finish()
        }

        binding.buttonBC2.setOnClickListener {
            val bTitle = binding.memotitle.text.toString()
            val bText = binding.memotext.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.RecRef
                .push()
                .setValue(boardModel(bTitle, bText, time, uid))

            Toast.makeText(this, "입력 완료", Toast.LENGTH_LONG).show()

            finish()
        }
    }
}
