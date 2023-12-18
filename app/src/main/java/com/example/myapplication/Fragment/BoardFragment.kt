package com.example.myapplication.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.board.*
import com.example.myapplication.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    private lateinit var binding: FragmentBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_homeFragment)
        }
        binding.calendarTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_memoFragment)
        }
        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_mapFragment)
        }
        binding.accountTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_accountFragment)
        }

        binding.dog.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_dogFragment)
        }

        binding.dogCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_dogFragment)
        }
        binding.cat.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_catFragment)
        }

        binding.catCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_catFragment)
        }
        binding.rabbit.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_rabbitFragment)
        }

        binding.rabbitCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_rabbitFragment)
        }

        binding.birld.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_birldFragment)
        }

        binding.birldCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_birldFragment)
        }

        binding.reptile.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_reptileFragment)
        }

        binding.reptileCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_reptileFragment)
        }

        binding.fish.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_blankFragment)
        }

        binding.fishCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_blankFragment)
        }

        binding.question.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_queFragment)
        }

        binding.queCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_queFragment)
        }

        binding.recommend.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_recFragment)
        }

        binding.recCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_recFragment)
        }

        binding.free.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_freeFragment)
        }

        binding.freeCate.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardFragment_to_freeFragment)
        }

        return binding.root
    }
}