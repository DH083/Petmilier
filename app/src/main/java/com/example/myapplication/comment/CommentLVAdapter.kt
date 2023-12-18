package com.example.myapplication.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.myapplication.R

class CommentLVAdapter (val commentList: MutableList<commentModel>): BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
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
        title.text = commentList[position].commentTitle

        val time = view!!.findViewById<TextView>(R.id.date)
        time.text = commentList[position].commentTime

        return view!!
    }
}
