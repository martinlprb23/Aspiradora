// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// Created by Martin Robles. github martinlprb23
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        title = "Aspiradora automatica"
    ) {
        App()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(viewModel: MainViewModel = MainViewModel()) {

    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .background(Color(0XFF0078AA)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Aspiradora",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Image(
                                painter = painterResource("aspiradora.png", loader = ResourceLoader.Default),
                                contentDescription = "Emoji"
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "Campos limpios: ${64 - (viewModel.count.value)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Basura total: ${(viewModel.count.value)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )


                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Column {
                            if (viewModel.isLoading.value) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(25.dp), color = Color.White)
                                }
                            }

                            Button(
                                onClick = {
                                    // Valida si hay basura y si esta puesta la aspiradora
                                    if (viewModel.list.contains(1) && viewModel.list.contains(2))
                                        viewModel.isLoading.value = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults
                                    .buttonColors(backgroundColor = Color(0xFF3AB4F2)),
                                shape = RoundedCornerShape(10.dp),
                                enabled = !viewModel.isLoading.value
                            ) {
                                Text("Limpiar", color = Color.White)
                            }
                        }
                    }

                }
            }
            GridContent(viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
@Composable
fun GridContent(viewModel: MainViewModel) {

    val list = viewModel.list
    LazyVerticalGrid(
        cells = GridCells.Fixed(8),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.padding(10.dp),
        state = rememberLazyListState(),
    ) {

        items(64) { item ->

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .combinedClickable(onClick = {
                        if (!viewModel.isLoading.value) {
                            if (list[item] == 0) {
                                list[item] = 1
                                viewModel.count.value = viewModel.count.value + 1
                            } else if (list[item] == 1) {
                                list[item] = 0
                                viewModel.count.value = viewModel.count.value - 1
                            }
                            println("Basura en la posición: ${item}")
                        }
                    }, onLongClick = {
                        if (!viewModel.isLoading.value) {
                            val startIndex = list.indexOf(2)
                            if (startIndex > 0) list[startIndex] = 0
                            list[item] = 2
                        }
                    }),
                contentAlignment = Alignment.Center
            ) {
                if (list[item] == 1) {
                    Image(
                        painter = painterResource("emoji.png", loader = ResourceLoader.Default),
                        contentDescription = "Emoji",
                        modifier = Modifier.padding(10.dp).size(38.dp)
                    )
                } else if (list[item] == 2) {
                    Image(
                        painter = painterResource("aspiradora.png", loader = ResourceLoader.Default),
                        contentDescription = "Emoji",
                        modifier = Modifier.padding(8.dp).size(40.dp)
                    )
                } else {
                    Text(
                        text = item.toString(),
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }

    // Incia la limpia XD como las brujas jajaja

    if (viewModel.isLoading.value) viewModel.clean()
}






























