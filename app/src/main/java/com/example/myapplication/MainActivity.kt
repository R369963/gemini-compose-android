  package com.example.myapplication

import android.content.Context
import android.graphics.Paint.Align
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ChatGPTUI()
                }
            }
        }
    }
}


  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun ChatGPTUI() {
      var messages by remember { mutableStateOf(listOf("Hello! How can I help you today?")) }
      var newMessage by remember { mutableStateOf(TextFieldValue()) }

      // Compose UI structure
      Column(
          modifier = Modifier
              .fillMaxSize()
              .background(Color.White)
      ) {
          // Chat messages
          Box(
              modifier = Modifier
                  .weight(1f)
                  .padding(0.dp)
                  .background(Color.LightGray)
                  .verticalScroll(rememberScrollState())
          ) {
              MessageList(messages)
          }

          // User input
          Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(0.dp)
          ) {
              OutlinedTextField(
                  value = newMessage,
                  onValueChange = {
                      newMessage = it
                  },
                  textStyle = MaterialTheme.typography.bodySmall,
                  modifier = Modifier
                      .weight(1f)
                      .padding(end = 8.dp)
              )

              IconButton(
                  onClick = {
                      if (newMessage.text.isNotEmpty()) {
                          messages = messages + newMessage.text
                          // Add logic here to send the message to ChatGPT and receive a response
                          newMessage = TextFieldValue("")
                      }
                  }
              ) {
                  Icon(
                      painter = painterResource(id = R.drawable.baseline_send_24),
                      contentDescription = null,
                      modifier = Modifier.size(32.dp)
                  )
              }
          }
      }
  }

  @Composable
  fun MessageList(messages: List<String>) {
      Column {
          messages.forEach {
              Text(
                  text = it,
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(8.dp)
                      .background(Color.Gray),
                  style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
              )
          }
      }
  }

  // Entry point for the Compose app
