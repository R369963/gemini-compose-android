  package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Part
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodels.GptViewModel

  class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ChatGPTUI(GptViewModel())
                }
            }
        }
    }
}


  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun ChatGPTUI(viewModel: GptViewModel) {
      var arrayList:ArrayList<Part> = ArrayList()
      arrayList.add(Part("Hello! How can I help you today?"))
       viewModel.getGptList("hi")
      val listOfResult  =
          arrayList

          //viewModel.results.value[0].content.parts;
      var messages by remember { mutableStateOf(listOfResult) }
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
                  .background(Color.Black)
                  .verticalScroll(rememberScrollState())
          ) {
              if(messages.isNotEmpty()){
                  MessageList(messages)
              }

          }

          Box(
              modifier = Modifier
                  .padding(0.dp)
                  .background(Color.Black)
          ) {
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(0.dp)
              ) {
                  OutlinedTextField(
                       keyboardOptions = KeyboardOptions(
                          imeAction = ImeAction.Done
                      ),
                      value = newMessage,
                      onValueChange = {
                          newMessage = it
                      },

                      textStyle = TextStyle(
                          color = Color.White,
                          fontStyle = FontStyle.Normal,
                          fontWeight = FontWeight.W500
                      ),
                      modifier = Modifier
                          .weight(1f)
                          .padding(end = 8.dp),
                      shape = RoundedCornerShape(20.dp),
                      trailingIcon = {
                          IconButton(
                              onClick = {
                                  viewModel.getGptList(newMessage.text)
                                  if (newMessage.text.isNotEmpty()) {
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

                  )


              }
          }
      }
  }

  @Composable
  fun MessageList(messages: List<Part>) {
      LazyColumn{
           items(items = messages){
               Text(
                   text = it.text,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp),

                   style = MaterialTheme.typography.bodySmall.copy(color = Color.Black)
               )
           }
      }

  }

