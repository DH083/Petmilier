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
import com.example.myapplication.board.*
import com.example.myapplication.databinding.FragmentDogBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DogFragment : Fragment() {

    private lateinit var binding: FragmentDogBinding

    private var Tag = BoardFragment::class.java

    private var boardDataList = mutableListOf<boardModel>()

    private lateinit var boardLVAdapter: BoardLVAdapter

    private val boardKeyList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dog, container, false)

        boardLVAdapter = BoardLVAdapter(boardDataList)
        binding.itemlistview.adapter = boardLVAdapter

        binding.itemlistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, BoardWatchActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)
        }

        binding.write.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_dogFragment_to_boardFragment)
        }

        getFBBoardData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                    Log.d(Tag.toString(), dataModel.toString())

                }

                boardKeyList.reverse()
                boardDataList.reverse()
                boardLVAdapter.notifyDataSetChanged()
                Log.d(Tag.toString(), boardDataList.toString())

            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
}