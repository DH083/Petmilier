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
import com.example.myapplication.board.recommend.RecActivity
import com.example.myapplication.board.recommend.RecWatchActivity
import com.example.myapplication.board.reptile.ReptileWatchActivity
import com.example.myapplication.board.reptile.ReptileWriteActivity
import com.example.myapplication.databinding.FragmentRecBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RecFragment : Fragment() {
    private lateinit var binding: FragmentRecBinding
    private var recDataList = mutableListOf<boardModel>()
    private lateinit var recLVAdapter: BoardLVAdapter
    private val recKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rec, container, false)

        recLVAdapter = BoardLVAdapter(recDataList)
        binding.reclistview.adapter = recLVAdapter

        binding.reclistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, RecWatchActivity::class.java)
            intent.putExtra("key", recKeyList[position])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, RecActivity::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_recFragment_to_boardFragment)
        }

        getFBRecData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBRecData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                recDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    recDataList.add(item!!)
                    recKeyList.add(dataModel.key.toString())
                }

                recKeyList.reverse()
                recDataList.reverse()
                recLVAdapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.RecRef.addValueEventListener(postListener)
    }
}