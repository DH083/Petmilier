package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.ActivityCatEditBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CatEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatEditBinding
    private lateinit var key: String
    private val Tag = CatEditActivity::class.java
    private lateinit var writerUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cat_edit)

        key = intent.getStringExtra("key").toString()

        getCatData(key)

        binding.backbutton2.setOnClickListener {
            finish()
        }

        binding.buttonBC2.setOnClickListener {
            editCatData(key)
        }
    }

    private fun editCatData(key: String) {
        FBRef.catRef
            .child(key)
            .setValue(
                boardModel(binding.memotitle.text.toString(),
                    binding.memotext.text.toString(), FBAuth.getTime(),
                    FBAuth.getUid())
            )

        Toast.makeText(this, "수정 완료", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getCatData(key: String){
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
        FBRef.catRef.child(key).addValueEventListener(postListener)
    }

}