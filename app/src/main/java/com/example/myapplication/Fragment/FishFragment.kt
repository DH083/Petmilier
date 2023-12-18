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
import com.example.myapplication.board.fish.FishWatchActivity
import com.example.myapplication.board.fish.FishWriteActivity
import com.example.myapplication.board.reptile.ReptileWatchActivity
import com.example.myapplication.board.reptile.ReptileWriteActivity
import com.example.myapplication.databinding.FragmentFishBinding
import com.example.myapplication.databinding.FragmentReptileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BlankFragment : Fragment() {
    private lateinit var binding: FragmentFishBinding

    private var Tag = BlankFragment::class.java

    private var fishDataList = mutableListOf<boardModel>()

    private lateinit var fishLVAdapter: BoardLVAdapter

    private val fishKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fish, container, false)

        fishLVAdapter = BoardLVAdapter(fishDataList)
        binding.fishlistview.adapter = fishLVAdapter

        binding.fishlistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, FishWatchActivity::class.java)
            intent.putExtra("key", fishKeyList[position])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, FishWriteActivity::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_blankFragment_to_boardFragment)
        }

        getFBFishData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBFishData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                fishDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    fishDataList.add(item!!)
                    fishKeyList.add(dataModel.key.toString())

                    Log.d(Tag.toString(), dataModel.toString())

                }

                fishKeyList.reverse()
                fishDataList.reverse()
                fishLVAdapter.notifyDataSetChanged()
                Log.d(Tag.toString(), fishDataList.toString())

            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.fishRef.addValueEventListener(postListener)
    }
}