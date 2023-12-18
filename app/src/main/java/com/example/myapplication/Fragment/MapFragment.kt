package com.example.myapplication.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.myapplication.MapActivity
//import com.example.myapplication.MainActivity
//import com.example.myapplication.MapActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        binding.mapButton.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java)
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_homeFragment)
        }
        binding.boardTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_boardFragment)
        }
        binding.calendarTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_memoFragment)
        }
        binding.accountTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_accountFragment)
        }

        return binding.root
    }
}