package com.example.myapplication.board.reptile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityBirdEditBinding
import com.example.myapplication.databinding.ActivityReptileEditBinding
import com.example.myapplication.memoModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ReptileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReptileEditBinding
    private lateinit var key: String
    private lateinit var writerUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reptile_edit)

        key = intent.getStringExtra("key").toString()

        getRepData(key)

        binding.backbutton2.setOnClickListener {
            finish()
        }

        binding.buttonBC2.setOnClickListener {
            editRepData(key)
        }

    }

    private fun editRepData(key: String) {

        FBRef.ReptileRef
            .child(key)
            .setValue(
                boardModel(binding.memotitle.text.toString(),
                    binding.memotext.text.toString(), FBAuth.getTime(),
                    FBAuth.getUid())
            )
        Toast.makeText(this, "수정완료", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getRepData(key: String) {
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
        FBRef.ReptileRef.child(key).addValueEventListener(postListener)
    }
}