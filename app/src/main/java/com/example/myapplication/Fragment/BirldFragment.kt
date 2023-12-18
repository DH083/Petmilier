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
import com.example.myapplication.CatActivity
import com.example.myapplication.CatwatchActivity
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.R
import com.example.myapplication.board.BoardLVAdapter
import com.example.myapplication.board.bird.BirdWatchActivity
import com.example.myapplication.board.bird.BirdWriteActivity
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.FragmentBirldBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BirldFragment : Fragment() {
    private lateinit var binding: FragmentBirldBinding
    private var Tag = BirldFragment::class.java
    private var birdDataList = mutableListOf<boardModel>()
    private lateinit var birdLVAdapter: BoardLVAdapter
    private val birdKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_birld, container, false)

        birdLVAdapter = BoardLVAdapter(birdDataList)
        binding.birdlistview.adapter = birdLVAdapter

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_birldFragment_to_boardFragment)
        }

        binding.birdlistview.setOnItemClickListener { parent, view, positioin, id ->
            val intent = Intent(context, BirdWatchActivity::class.java)
            intent.putExtra("key", birdKeyList[positioin])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, BirdWriteActivity::class.java)
            startActivity(intent)
        }

        getFBBirdData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBBirdData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                birdDataList.clear()

                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    birdDataList.add(item!!)
                    birdKeyList.add(dataModel.key.toString())

                    Log.d(Tag.toString(), dataModel.toString())
                }

                birdKeyList.reverse()
                birdDataList.reverse()
                birdLVAdapter.notifyDataSetChanged()
                Log.d(Tag.toString(), birdDataList.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.birldRef.addValueEventListener(postListener)
    }
}