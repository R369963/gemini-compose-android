package com.example.myapplication.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiSerivce.RetrofitInstance
import com.example.myapplication.model.BardRequest
import com.example.myapplication.model.Candidate
import com.example.myapplication.model.Content
import com.example.myapplication.model.Part
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class GptViewModel:ViewModel(){
    private val apiService = RetrofitInstance.api
    val results : MutableState<List<Candidate>> =
        mutableStateOf(emptyList())
    fun getGptList(question:String){
        val requestBody = BardRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = "$question")
                    )
                )
            )
        )
        viewModelScope.launch {
            try {
                 val response  = apiService.getGptResponse(requestBody)
                if (response.isNotEmpty()){
                results.value = response
                }
            } catch (e:Exception){

            }
        }
    }
}