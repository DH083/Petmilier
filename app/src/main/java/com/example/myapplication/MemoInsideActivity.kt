package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.board.BoardEditActivity
import com.example.myapplication.databinding.ActivityMemoInsideBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MemoInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMemoInsideBinding

    private lateinit var key: String
    private var Tag = ActivityMemoInsideBinding::class.java

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_memo_inside)

        key = intent.getStringExtra("key").toString()
        getMemoData(key.toString())

        val delete = findViewById<Button>(R.id.deletebutton)

        delete.setOnClickListener {
            FBRef.memoRef.child(key).removeValue()
            Toast.makeText(this, "삭제 완료", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.backbutton2.setOnClickListener {
            finish()
        }

        binding.editbutton.setOnClickListener {
            val intent = Intent(this, MemoEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

    }

    private fun getMemoData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(memoModel::class.java)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.text
                    binding.wirteTime.text = dataModel!!.time


                } catch (e: Exception) {

                    Log.d(Tag.toString(), "삭제 완료")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        FBRef.memoRef.child(key).addValueEventListener(postListener)
    }
}
