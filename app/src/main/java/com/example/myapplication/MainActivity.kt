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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

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
        Box(
            modifier = Modifier

                .weight(1f)
                .padding(0.dp)
                .background(Color.Black)

        ) {
            MessageList(responseData, viewModel)
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
                        Row {
//                            /*  IconButton(onClick ={
//
//                              }) {
//                                  Icon(
//                                      painter = painterResource(id = R.drawable.outline_attachment_24),
//                                      contentDescription = null,
//                                      modifier = Modifier.size(32.dp),
//                                      tint = Color(0xFF7E50FF)
//                                  )
//                              }*/

                            IconButton(
                                onClick = {
                                    if (newMessage.text.isNotEmpty()) {
                                        viewModel.getGptList(newMessage.text)
                                        loadingIcon.value = true
                                        keyboardController?.hide()
                                        newMessage = TextFieldValue("")
                                    }
                                }
                            ) {
                                if (!loadingIcon.value) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_send_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp),
                                        tint = Color(0xFF7E50FF)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_stop_circle_24),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable {
                                                loadingIcon.value = false
                                            },
                                        tint = Color(0xFF8B20E9)
                                    )
                                }
                            }

                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MessageList(messages: MutableState<List<QuestionAnswer>>, viewMode: GptViewModel) {
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64String by remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = GetContent()) { uri: Uri? ->
        selectedImageUri = uri
        base64String = uri?.let { viewMode.uriToBase64(context.contentResolver, it) }
    }
    LaunchedEffect(messages.value.size) {
        lazyListState.scrollToItem(messages.value.size)
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
        Column {
            Text(
                text = "Hello, may I assist you",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
            )
            ImagePicker(
                selectedImageUri = selectedImageUri,
                onImageSelected = { launcher.launch("image/*") },
                viewMode = GptViewModel()
            )
        }
    }
}

@Composable
fun listWidget(questionAnswer: QuestionAnswer) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_question_mark_24),
                    contentDescription = "",
                    tint = Color(0xFF8B20E9)
                )
                Text(
                    text = questionAnswer.question,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),

                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
            SelectionContainer {
                Text(
                    text = questionAnswer.answer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Composable
fun ImagePicker(
    selectedImageUri: Uri?,
    onImageSelected: () -> Unit,
    viewMode: GptViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Black)
            .clickable { onImageSelected() }
            .clip(MaterialTheme.shapes.medium)
    ) {
        if (selectedImageUri != null) {
            val bitmap =
                viewMode.loadBitmapFromUri(LocalContext.current.contentResolver, selectedImageUri)
            Image(
                bitmap = bitmap?.asImageBitmap() ?: ImageBitmap(1, 1),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_attachment_24),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                    ,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Image",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White
                    ))
            }
        }
    }
}

