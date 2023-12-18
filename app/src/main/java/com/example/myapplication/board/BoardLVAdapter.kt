package com.example.myapplication.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.myapplication.R

class BoardLVAdapter (val boardList: MutableList<boardModel>): BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if(view == null) {

            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_recycle, parent, false)

        }

        val title = view!!.findViewById<TextView>(R.id.title)
        title.text = boardList[position].title

        val time = view!!.findViewById<TextView>(R.id.date)
        time.text = boardList[position].time

        return view!!
    }
}
