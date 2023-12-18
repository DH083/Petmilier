package com.example.myapplication.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.board.BoardLVAdapter
import com.example.myapplication.board.boardModel
import com.example.myapplication.board.question.QuestionWatchActivity
import com.example.myapplication.board.question.QuestionWriteActivity
import com.example.myapplication.databinding.FragmentQueBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class QueFragment : Fragment() {
    private lateinit var binding: FragmentQueBinding
    private var queDataList = mutableListOf<boardModel>()
    private lateinit var queLVAdapter: BoardLVAdapter
    private val queKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_que, container, false)

        queLVAdapter = BoardLVAdapter(queDataList)
        binding.quelistview.adapter = queLVAdapter

        binding.quelistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, QuestionWatchActivity::class.java)
            intent.putExtra("key", queKeyList[position])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, QuestionWriteActivity::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_queFragment_to_boardFragment)
        }

        getFBQueData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBQueData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                queDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    queDataList.add(item!!)
                    queKeyList.add(dataModel.key.toString())

                }

                queKeyList.reverse()
                queDataList.reverse()
                queLVAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.QueRef.addValueEventListener(postListener)
    }
}