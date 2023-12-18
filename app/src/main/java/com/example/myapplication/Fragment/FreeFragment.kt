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
import com.example.myapplication.board.fish.FishWriteActivity
import com.example.myapplication.board.free.FreeWatchActivity
import com.example.myapplication.board.free.FreeWriteActivity
import com.example.myapplication.databinding.FragmentFreeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FreeFragment : Fragment() {
    private lateinit var binding: FragmentFreeBinding
    private var Tag = FreeFragment::class.java
    private var freeDataList = mutableListOf<boardModel>()
    private lateinit var freeLVAdapter: BoardLVAdapter
    private val freeKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_free, container, false)

        freeLVAdapter = BoardLVAdapter(freeDataList)
        binding.freelistview.adapter = freeLVAdapter

        binding.freelistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, FreeWatchActivity::class.java)
            intent.putExtra("key", freeKeyList[position])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, FreeWriteActivity::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_freeFragment_to_boardFragment)
        }

        getFBFreeData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBFreeData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                freeDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    freeDataList.add(item!!)
                    freeKeyList.add(dataModel.key.toString())

                    Log.d(Tag.toString(), dataModel.toString())

                }

                freeKeyList.reverse()
                freeDataList.reverse()
                freeLVAdapter.notifyDataSetChanged()
                Log.d(Tag.toString(), freeDataList.toString())

            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.freeRef.addValueEventListener(postListener)
    }
}