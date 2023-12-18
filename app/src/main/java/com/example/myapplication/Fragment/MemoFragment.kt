package com.example.myapplication.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.myapplication.*
import com.example.myapplication.FireBase.FBAuth
import com.example.myapplication.FireBase.FBRef
import com.example.myapplication.databinding.FragmentMemoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MemoFragment : Fragment() {

    private lateinit var binding: FragmentMemoBinding

    private var memoDataList = mutableListOf<memoModel>()

    private lateinit var memoRVAdapter: MemoRVAdapter

    private val memoKeyList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo, container, false)

        memoRVAdapter = MemoRVAdapter(memoDataList)
        binding.itemlistview.adapter = memoRVAdapter

        binding.itemlistview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, MemoInsideActivity::class.java)
            intent.putExtra("key", memoKeyList[position])
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_memoFragment_to_homeFragment)
        }
        binding.boardTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_memoFragment_to_boardFragment)
        }
        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_memoFragment_to_mapFragment)
        }
        binding.accountTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_memoFragment_to_accountFragment)
        }

        binding.write.setOnClickListener {
            val intent = Intent(context, Calendar2Activity::class.java)
            startActivity(intent)
        }

        getFBMemodData()

        return binding.root
    }

    //파이어베이스 연결
    private fun getFBMemodData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                memoDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(memoModel::class.java)
                    memoDataList.add(item!!)
                    memoKeyList.add(dataModel.key.toString())

                }

                memoKeyList.reverse()
                memoDataList.reverse()
                memoRVAdapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.memoRef.addValueEventListener(postListener)
    }
}