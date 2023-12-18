package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityBoardInsideBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var key: String
    private var Tag = ActivityBoardInsideBinding::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        key = intent.getStringExtra("key").toString()
        getBoardData(key.toString())
    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(boardModel::class.java)

                    binding.writeTitle.text = dataModel!!.title
                    binding.writeText.text = dataModel!!.text
                    binding.writeTime.text = dataModel!!.time
                } catch (e: Exception) {
                    Log.d(Tag.toString(), "삭제 완료")
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}

