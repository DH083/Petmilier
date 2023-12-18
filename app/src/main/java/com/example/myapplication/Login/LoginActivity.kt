package com.example.myapplication.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.Fragment.BoardFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val btnLog= findViewById<Button>(R.id.btn_log)
        btnLog.setOnClickListener {

            val email = findViewById<TextInputEditText>(R.id.Area_email)
            val password = findViewById<TextInputEditText>(R.id.Area_pass)

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //로그인 성공시 메인화면으로 이동
                        val intent = Intent(this, MainActivity::class.java)
                        finishAffinity()
                        startActivity(intent)
                    } else {
                        // 로그인 실패
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }


    }
}