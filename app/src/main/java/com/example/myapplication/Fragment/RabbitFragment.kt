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
import com.example.myapplication.RabbitActivity
import com.example.myapplication.RabbitWatchActivity
import com.example.myapplication.board.BoardLVAdapter
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.FragmentRabbitBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RabbitFragment : Fragment() {
    private lateinit var binding: FragmentRabbitBinding
    private var Tag = RabbitFragment::class.java
    private var rabDataList = mutableListOf<boardModel>()
    private lateinit var rabLVAdapter: BoardLVAdapter
    private val rabKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rabbit, container, false)

        rabLVAdapter = BoardLVAdapter(rabDataList)
        binding.rabbitlistview.adapter = rabLVAdapter

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_rabbitFragment_to_boardFragment)
        }

        binding.rabbitlistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, RabbitWatchActivity::class.java)
            intent.putExtra("key", rabKeyList[position])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, RabbitActivity::class.java)
            startActivity(intent)
        }

        getRabData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getRabData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rabDataList.clear()

                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    rabDataList.add(item!!)
                    rabKeyList.add(dataModel.key.toString())

                    Log.d(Tag.toString(), dataModel.toString())
                }

                rabKeyList.reverse()
                rabDataList.reverse()
                rabLVAdapter.notifyDataSetChanged()
                Log.d(Tag.toString(), rabDataList.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.rabbitRef.addValueEventListener(postListener)
    }
}