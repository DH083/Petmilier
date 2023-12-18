package com.example.myapplication

import com.example.maps.data.AnimalHospital
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


class OpenAPI {
    companion object{
        val DOMAIN = "http://openapi.seoul.go.kr:8088/"
        val API_KEY = "54486f72466262613636415555474a"
    }
}

interface OpenService{
    @GET("{API_KEY}/json/LOCALDATA_020301/1/{end}")
    fun getHospital(@Path("API_KEY")key:String, @Path("end") limit:Int) : Call<AnimalHospital>
}