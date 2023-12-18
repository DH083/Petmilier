package com.example.myapplication.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.Auth.UserDataModel
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDogEditBinding
import com.example.myapplication.memoModel
import com.example.myapplication.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dog_edit.*
import kotlinx.android.synthetic.main.activity_dog_watch.*

class DogEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDogEditBinding

    private lateinit var key: String

    private val Tag = DogEditActivity::class.java

    private lateinit var writerUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dog_edit)

        key = intent.getStringExtra("key").toString()

        getBoardData(key)

        binding.backbutton2.setOnClickListener {
            finish()
        }

        binding.buttonBC2.setOnClickListener {
            editBoardData(key)
        }

    }

    private fun editBoardData(key: String) {

        FBRef.boardRef
            .child(key)
            .setValue(
                boardModel(binding.memotitle.text.toString(),
                    binding.memotext.text.toString(),
                    FBAuth.getTime(),
                    FBAuth.getUid())
            )
        Toast.makeText(this, "수정완료", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getBoardData(key: String) {
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
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}