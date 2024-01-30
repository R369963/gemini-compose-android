package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.QuestionAnswer
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodels.GptViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatGPTUI(GptViewModel())
                }
            }
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatGPTUI(viewModel: GptViewModel) {
     val loadingIcon = viewModel.loadingIcon
      val responseData = viewModel.results
      var newMessage by remember { mutableStateOf(TextFieldValue()) }
      val keyboardController = LocalSoftwareKeyboardController.current

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
            //.verticalScroll(rememberScrollState())
        ) {
            MessageList(responseData)

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
                                if (newMessage.text.isNotEmpty()) {
                                    viewModel.getGptList(newMessage.text)
                                    keyboardController?.hide()
                                    newMessage = TextFieldValue("")
                                }
                            }
                        ) {
                           if(!loadingIcon.value)
                           {
                               Icon(
                                   painter = painterResource(id = R.drawable.baseline_send_24),
                                   contentDescription = null,
                                   modifier = Modifier.size(32.dp)
                               )
                           }else{
                               AnimatedVectorDrawable(loadingIcon.value)
                           }
                        }
                    }
                )
             }
        }
    }
}
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable

fun AnimatedVectorDrawable(value: Boolean) {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.cloud_network)
   Image(
        painter = rememberAnimatedVectorPainter(image, value),
        contentDescription = "Timer",
        modifier = Modifier.clickable {
            // Handle click or remove the clickable modifier if not needed
        },
        contentScale = ContentScale.Crop
    )
}
@Composable
fun MessageList(messages: MutableState<List<QuestionAnswer>>) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(messages.value.size) {
        // Automatically scroll to the last item when a new message is added
        lazyListState.scrollToItem(messages.value.size  )
    }

    if (messages.value.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            state = lazyListState
        ) {
            items(items = messages.value) {
                listWidget(it)
            }
        }
    } else {
        Text(
            text = "Hello, may I assist you",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
        )
    }
}

@Composable
fun listWidget(questionAnswer: QuestionAnswer) {
    Box (
        modifier = Modifier.fillMaxWidth()

    ){
       Column {
           Text(text =questionAnswer.question,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(8.dp),

               style = MaterialTheme.typography.bodySmall.copy(color = Color.White,
                   fontSize = 16.sp) )
           Text(
               text =   questionAnswer.answer,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(8.dp),
               style = MaterialTheme.typography.bodySmall.copy(color = Color.White,
                   fontSize = 16.sp)
           )
       }
    }
}

