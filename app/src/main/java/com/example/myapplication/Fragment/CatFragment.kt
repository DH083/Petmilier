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
import com.example.myapplication.board.boardModel
import com.example.myapplication.databinding.FragmentCatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CatFragment : Fragment() {
    private lateinit var binding: FragmentCatBinding
    private var Tag = CatFragment::class.java
    private var catDataList = mutableListOf<boardModel>()
    private lateinit var catLVAdapter: BoardLVAdapter
    private val catKeyList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cat, container, false)

        catLVAdapter = BoardLVAdapter(catDataList)
        binding.catListview.adapter = catLVAdapter

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_catFragment_to_boardFragment)
        }

        binding.catListview.setOnItemClickListener { parent, view, positioin, id ->
            val intent = Intent(context, CatwatchActivity::class.java)
            intent.putExtra("key", catKeyList[positioin])
            startActivity(intent)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(context, CatActivity::class.java)
            startActivity(intent)
        }

        getFBCatData()
        return binding.root
    }

    //파이어베이스 연결
    private fun getFBCatData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                catDataList.clear()

                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(boardModel::class.java)
                    catDataList.add(item!!)
                    catKeyList.add(dataModel.key.toString())

                    Log.d(Tag.toString(), dataModel.toString())
                }

                catKeyList.reverse()
                catDataList.reverse()
                catLVAdapter.notifyDataSetChanged()
                Log.d(Tag.toString(), catDataList.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.catRef.addValueEventListener(postListener)
    }
}