package com.example.myapplication.data

import com.example.maps.data.RESULT
import com.example.myapplication.data.Row
import com.google.gson.annotations.SerializedName

data class LOCALDATA020301(
    @SerializedName("list_total_count")
    val listTotalCount: Int?,
    @SerializedName("RESULT")
    val rESULT: RESULT?,
    @SerializedName("row")
    val row: List<Row?>?
)