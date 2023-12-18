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
import com.example.myapplication.board.BoardWatchActivity
import com.example.myapplication.board.BoardWriteActivity
import com.example.myapplication.board.boardModel
import com.example.myapplication.board.reptile.ReptileWatchActivity
import com.example.myapplication.board.reptile.ReptileWriteActivity
import com.example.myapplication.databinding.FragmentDogBinding
import com.example.myapplication.databinding.FragmentReptileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ReptileFragment : Fragment() {
    private lateinit var binding: FragmentReptileBinding

    private var Tag = ReptileFragment::class.java

    private var reptileDataList = mutableListOf<boardModel>()

    private lateinit var reptileLVAdapter: BoardLVAdapter

    private val reptileKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reptile, container, false)

        reptileLVAdapter = BoardLVAdapter(reptileDataList)
        binding.reptilelistview.adapter = reptileLVAdapter

        binding.reptilelistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, ReptileWatchActivity::class.java)
            intent.putExtra("key", reptileKeyList[position])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, ReptileWriteActivity::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_reptileFragment_to_boardFragment)
        }

        getFBRepData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBRepData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                reptileDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    reptileDataList.add(item!!)
                    reptileKeyList.add(dataModel.key.toString())

                    Log.d(Tag.toString(), dataModel.toString())

                }

                reptileKeyList.reverse()
                reptileDataList.reverse()
                reptileLVAdapter.notifyDataSetChanged()
                Log.d(Tag.toString(), reptileDataList.toString())

            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.ReptileRef.addValueEventListener(postListener)
    }
}