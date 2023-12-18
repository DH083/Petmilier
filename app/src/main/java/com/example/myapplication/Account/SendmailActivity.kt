package com.example.myapplication.Account

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySendmailBinding

class SendmailActivity : AppCompatActivity() {

    lateinit var binding : ActivitySendmailBinding
    lateinit var adr : EditText
    lateinit var txt1 : EditText
    lateinit var txt2 : EditText

    lateinit var sendBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendmail)

        adr = findViewById(R.id.adr)
        txt1 = findViewById(R.id.txt1)
        txt2 = findViewById(R.id.txt2)
        sendBtn = findViewById(R.id.sendBtn)

        sendBtn.setOnClickListener {
            val email = adr.text.toString().trim()
            val subject = txt1.text.toString().trim()
            val message = txt2.text.toString().trim()

            var i = Intent(Intent.ACTION_SEND)
            i.data = Uri.parse("Mail to: ")
            i.type = "text/plain"

            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            i.putExtra(Intent.EXTRA_SUBJECT, "[건의사항]")
            i.putExtra(Intent.EXTRA_TEXT, "[아이디]" +"\n"+ "\n"+
                    "[건의내용]")

            startActivity(Intent.createChooser(i, "메일을 선택해주세요"))
        }
    }
}