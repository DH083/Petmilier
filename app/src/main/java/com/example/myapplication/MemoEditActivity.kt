package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.databinding.ActivityMemoEditBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MemoEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMemoEditBinding

    private lateinit var key: String

    private lateinit var writerUid: String

    private var Tag = ActivityMemoEditBinding::class.java


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_edit)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_memo_edit)

        key = intent.getStringExtra("key").toString()

        getMemoData(key)

        binding.editButton.setOnClickListener {
            editMemoData(key)
        }

        binding.backbutton2.setOnClickListener {
            finish()
        }
    }

    private fun editMemoData(key: String) {

        FBRef.memoRef
            .child(key)
            .setValue(memoModel(binding.memotitle.text.toString(),
                binding.memotext.text.toString(),FBAuth.getTime(),
            FBAuth.getUid())
            )
        Toast.makeText(this, "수정완료", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getMemoData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(memoModel::class.java)

                binding.memotitle.setText(dataModel?.title)
                binding.memotext.setText(dataModel?.text)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        FBRef.memoRef.child(key).addValueEventListener(postListener)
    }

}