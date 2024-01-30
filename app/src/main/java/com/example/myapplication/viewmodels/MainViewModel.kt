package com.example.myapplication.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiSerivce.RetrofitInstance
import com.example.myapplication.model.BardRequest
import com.example.myapplication.model.Candidate
import com.example.myapplication.model.Content
import com.example.myapplication.model.Part
import com.example.myapplication.model.Root
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.math.log

class GptViewModel:ViewModel(){
    private val apiService = RetrofitInstance.api
    val results : MutableState<List<Part>> =
        mutableStateOf(emptyList())
     fun getGptList(question:String){
        Log.d("::","hit 1")
        val requestBody = BardRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = question)
                    )
                )
            )
        )
        viewModelScope.launch {
              try {
                val response = apiService.getGptResponse(requestBody)
                results.value += listOf( response.candidates[0].content.parts[0])
                Log.w("**", response.candidates[0].content.parts[0].text)
            }catch (_:Exception){
          }
         }
    }
}