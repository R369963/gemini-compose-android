package com.example.myapplication.apiSerivce

import com.example.myapplication.model.BardRequest
import com.example.myapplication.model.Candidate
import com.example.myapplication.model.Content
import com.example.myapplication.model.Part
import com.example.myapplication.model.Root
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface  ApiService{
    @POST("models/gemini-pro:generateContent?key=AIzaSyD88I57qhC-GznvLD1RJXi2cxXIUriWfMo")
    suspend fun  getGptResponse (
        @Body request: BardRequest
    ):   Root
}