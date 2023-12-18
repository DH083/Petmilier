package com.example.myapplication.board.reptile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityReptileWriteBinding

class ReptileWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReptileWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_reptile_write)

        binding.backbutton2.setOnClickListener {
            finish()
        }

        binding.buttonBC2.setOnClickListener {
            val bTitle = binding.memotitle.text.toString()
            val bText = binding.memotext.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.ReptileRef
                .push()
                .setValue(boardModel(bTitle, bText, time, uid))

            Toast.makeText(this, "입력 완료", Toast.LENGTH_LONG).show()

            finish()
        }
    }
}
