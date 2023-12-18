package com.example.myapplication.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDogBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogBinding

    val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dog)

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
