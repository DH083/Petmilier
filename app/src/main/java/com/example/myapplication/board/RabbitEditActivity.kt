package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityRabbitEditBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RabbitEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRabbitEditBinding
    private lateinit var key: String
    private val Tag = RabbitEditActivity::class.java
    private lateinit var writerUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rabbit_edit)

        key = intent.getStringExtra("key").toString()

        getRabData(key)

        binding.backbutton2.setOnClickListener {
            finish()
        }

        binding.buttonBC2.setOnClickListener {
            editRabData(key)
        }
    }

    // 뒤로가기 2번
    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        Log.d("TAG", "뒤로가기")

        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }

        // 처음 클릭 메시지
        Toast.makeText(this, "종료하시려면 버튼을 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    private fun editRabData(key: String) {
        FBRef.rabbitRef
            .child(key)
            .setValue(
                boardModel(binding.memotitle.text.toString(),
                    binding.memotext.text.toString(),
                    FBAuth.getTime(), FBAuth.getUid())
            )

        Toast.makeText(this, "수정 완료", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getRabData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataModel = dataSnapshot.getValue(boardModel::class.java)
                binding.memotitle.setText(dataModel!!.title)
                binding.memotext.setText(dataModel!!.text)
                writerUid = dataModel!!.uid
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.rabbitRef.child(key).addValueEventListener(postListener)
    }
}