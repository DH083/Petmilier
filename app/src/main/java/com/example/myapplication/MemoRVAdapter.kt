package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MemoRVAdapter(val memoList: MutableList<memoModel>): BaseAdapter() {
    override fun getCount(): Int {
        return memoList.size
    }

    override fun getItem(position: Int): Any {
        return memoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if(view == null) {

            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_recycler, parent, false)

        }

        val title = view?.findViewById<TextView>(R.id.title)
        title!!.text = memoList[position].title

        val time = view?.findViewById<TextView>(R.id.date)
        time!!.text = memoList[position].time

        return view!!
    }
}

