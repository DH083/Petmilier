package com.example.myapplication.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBoardWriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        val backbutton = findViewById<Button>(R.id.backbutton2)

        backbutton.setOnClickListener {
            finish()
        }

        binding.buttonBC2.setOnClickListener {
            val bTitle = binding.memotitle.text.toString()
            val bText = binding.memotext.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.boardRef
                .push()
                .setValue(boardModel(bTitle, bText, time, uid))

            Toast.makeText(this, "입력 완료", Toast.LENGTH_LONG).show()

            finish()
        }
    }
}
