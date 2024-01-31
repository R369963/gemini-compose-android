package com.example.myapplication.viewmodels

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiSerivce.RetrofitInstance
import com.example.myapplication.model.BardRequest
import com.example.myapplication.model.Candidate
import com.example.myapplication.model.Content
import com.example.myapplication.model.Part
import com.example.myapplication.model.QuestionAnswer
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
    val results : MutableState<List<QuestionAnswer>> =
        mutableStateOf(emptyList())
    val loadingIcon:MutableState<Boolean> = mutableStateOf(false)

     @SuppressLint("SuspiciousIndentation")
     fun getGptList(question:String){
        Log.d("::","hit 1")
        loadingIcon.value =true
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
          //  loadingIcon.value = true
              try {
                val response = apiService.getGptResponse(requestBody)
                  if(response.candidates[0].content.parts.isNotEmpty()) {
                      results.value += listOf(
                          QuestionAnswer(
                              question = question,
                              answer = response.candidates[0].content.parts[0].text
                          )
                      )
                      Log.w("**", response.candidates[0].content.parts[0].text)
                     loadingIcon.value = false
                  }
            }catch (_:Exception){
          }
         }
    }
      fun uriToBase64(contentResolver: ContentResolver, uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes()
        inputStream?.close()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

      fun loadBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }
}
