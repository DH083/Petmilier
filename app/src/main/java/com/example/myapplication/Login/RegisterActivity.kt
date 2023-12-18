package com.example.myapplication.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.Account.PetImageActivity
import com.example.myapplication.Auth.PetDataModel
import com.example.myapplication.Auth.UserDataModel
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.example.myapplication.utils.FirebaseRef
import com.google.firebase.auth.ktx.auth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding

    private var email = ""
    private var password = ""
    private var passchk = ""
    private var nickname = ""
    private var uid  = ""
    private var petname  = ""
//    private var typeV = ""
    private var typeDt = ""
    private var sex = ""
    private var bir = ""
    private var meet = ""
    private var del = ""
    private var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnSign = findViewById<Button>(R.id.btn_sign)
        btnSign.setOnClickListener {

            email = findViewById<TextInputEditText>(R.id.area_email).text.toString()
            password = findViewById<TextInputEditText>(R.id.area_pass).text.toString()
            passchk = findViewById<TextInputEditText>(R.id.area_passchk).text.toString()
            nickname = findViewById<TextInputEditText>(R.id.area_nickname).text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (email == "" || password == "" || passchk == "" || nickname == "") Toast.makeText(
                        this, "회원정보를 전부 입력해주세요.",
                        Toast.LENGTH_SHORT
                    ).show() else {
                        if (password == passchk) {
                            // 회원가입 성공시
                            val user = auth.currentUser
                            uid = user?.uid.toString()

                            //유저 데이터 저장
                            val userModel = UserDataModel(
                                uid,
                                nickname
                            )
                            FirebaseRef.userInfoRef.child(uid).setValue(userModel)

                            //펫 데이터 생성
                            val petModel = PetDataModel (
                                uid, petname, typeDt, sex, bir, meet, del, date
                            )
                            FirebaseRef.petInfoRef.child(uid).setValue(petModel)

                            val intent = Intent(this, PetImageActivity::class.java)
                            finishAffinity()
                            startActivity(intent)
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this, "비밀번호가 일치하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }
}