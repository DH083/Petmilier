package com.example.myapplication.board.question

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.board.bird.BirdEditActivity
import com.example.myapplication.board.boardModel
import com.example.myapplication.comment.CommentLVAdapter
import com.example.myapplication.comment.commentModel
import com.example.myapplication.databinding.ActivityBirdWatchBinding
import com.example.myapplication.databinding.ActivityQuestionWatchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class QuestionWatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionWatchBinding
    private lateinit var key: String
    private var Tag = ActivityQuestionWatchBinding::class.java
    private val commentDataList = mutableListOf<commentModel>()
    private lateinit var commentAdapter: CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_watch)

        key = intent.getStringExtra("key").toString()
        getQueData(key.toString())

        binding.imageView.setOnClickListener {
            finish()
        }

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        binding.commentsend.setOnClickListener {
            insertComment(key)
        }

        getCommentData(key)
        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentlist.adapter =commentAdapter

    }

    fun getCommentData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentDataList.clear()
                for(dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(commentModel::class.java)
                    commentDataList.add(item!!)
                }
                commentAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    fun insertComment(key: String) {
        //comment
        //  -BoardKey
        //   -CommentKey
        //    -CommentData
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(commentModel(binding.commentArea.text.toString(),
                FBAuth.getTime(), FBAuth.getUid()))
        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_LONG).show()
    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBUilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val alertDialog =  mBUilder.show()

        alertDialog.findViewById<Button>(R.id.delete)?.setOnClickListener {
            FBRef.QueRef.child(key).removeValue()
            Toast.makeText(this, "삭제 완료", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
            finish()
        }

        alertDialog.findViewById<Button>(R.id.cancel)?.setOnClickListener {
            val intent = Intent(this, QuestionEditActivity::class.java)
            intent.putExtra("key", key)
            alertDialog.dismiss()
            startActivity(intent)
        }

    }

    private fun getQueData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(boardModel::class.java)

                    binding.writeTitle.text = dataModel!!.title
                    binding.writeText.text = dataModel!!.text
                    binding.writeTime.text = dataModel!!.time

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    if(myUid.equals(writerUid)) {
                        binding.boardSettingIcon.isVisible = true
                    } else {

                    }

                } catch (e: Exception) {
                    Log.d(Tag.toString(), "삭제 완료")
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.QueRef.child(key).addValueEventListener(postListener)
    }

}