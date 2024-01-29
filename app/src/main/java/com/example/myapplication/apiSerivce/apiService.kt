package com.example.myapplication.apiSerivce

import com.example.myapplication.model.BardRequest
import com.example.myapplication.model.Candidate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface  ApiService{
    @POST("gemini-pro:generateContent?key=AIzaSyD88I57qhC-GznvLD1RJXi2cxXIUriWfMo")
    suspend fun  getGptResponse (
        @Body request: BardRequest
    ):List<Candidate>
}