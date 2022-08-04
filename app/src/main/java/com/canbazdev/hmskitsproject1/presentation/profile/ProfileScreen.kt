package com.canbazdev.hmskitsproject1.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.canbazdev.hmskitsproject1.R

/*
*   Created by hamzacanbaz on 8/1/2022
*/

@Composable
fun GetProfileScreen(profileViewModel: ProfileViewModel) {
    val userId by profileViewModel.userId.collectAsState()
    val userEmail by profileViewModel.userEmail.collectAsState()
    val currentLandmarks by profileViewModel.currentLandmarks.collectAsState(profileViewModel.landmarks.value)

    val isLottiePlaying by remember { mutableStateOf(true) }
    val animationSpeed by remember { mutableStateOf(1f) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.avatar))
    val lottieAnimation by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isLottiePlaying,
        speed = animationSpeed,
        restartOnPlay = false
    )

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3)
            ) {

                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = lottieAnimation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    UserText(text = userId)
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    UserText(text = userEmail)
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    ShowImagesOrToDo(onChangeLandmarks = {
                        println("SIZEEEE" + currentLandmarks.size)
                        if (it == 0) profileViewModel.setSharedLandmarks() else profileViewModel.setWishListLandmarks()
                    })
                }

                items(3 * (currentLandmarks.size / 3) + 3) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(0.dp, 12.dp, 0.dp, 12.dp)
                    ) {
                        if (it < currentLandmarks.size) {
                            currentLandmarks[it].landmarkImage?.let { image ->
                                GridImage(image = image)
                            }

                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LogoutButton(profileViewModel)
                }


            }
        }
    }
}

@Composable
fun UserText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 24.dp, 0.dp, 0.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun GridImage(image: String) {
    com.skydoves.landscapist.glide.GlideImage(
        imageModel = image,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
    )
}

@Composable
fun LogoutButton(viewModel: ProfileViewModel) {
    Button(
        onClick = {
            viewModel.logOut()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(36.dp, 12.dp)
    ) {
        Text(text = "Log Out")
    }
}

@Composable
fun ShowImagesOrToDo(onChangeLandmarks: (id: Int) -> Unit) {
    val cornerRadius = 8.dp
    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                selectedIndex = 0
                onChangeLandmarks.invoke(0)
                println(selectedIndex)
            },
            shape =
            RoundedCornerShape(
                topStart = cornerRadius,
                topEnd = 0.dp,
                bottomStart = cornerRadius,
                bottomEnd = 0.dp
            ),
            border = BorderStroke(
                1.dp,
                Color(0xFFF48FB1)
            ), colors = if (selectedIndex == 0) {
                // selected colors
                ButtonDefaults.outlinedButtonColors(backgroundColor = Color(0xFFF48FB1))
            } else {
                // not selected colors
                ButtonDefaults.outlinedButtonColors(backgroundColor = Color.White)
            }
        ) {
            Text(
                text = "Shared",
                color = if (selectedIndex == 0) {
                    Color.White
                } else {
                    Color.Gray
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

        }
        OutlinedButton(
            onClick = {
                selectedIndex = 1
                onChangeLandmarks.invoke(1)
                println(selectedIndex)
            },
            shape =
            RoundedCornerShape(
                topStart = 0.dp,
                topEnd = cornerRadius,
                bottomStart = 0.dp,
                bottomEnd = cornerRadius
            ),
            border = BorderStroke(
                1.dp, Color(0xFFF48FB1)
            ),
            colors = if (selectedIndex == 1) {
                // selected colors
                ButtonDefaults.outlinedButtonColors(backgroundColor = Color(0xFFF48FB1))
            } else {
                // not selected colors
                ButtonDefaults.outlinedButtonColors(backgroundColor = Color.White)
            }
        ) {
            Text(
                text = "To Go",
                color = if (selectedIndex == 1) {
                    Color.White
                } else {
                    Color.Gray
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(showBackground = true)
@Composable
fun MyPreview() {
    MaterialTheme {
        ShowImagesOrToDo(onChangeLandmarks = {})

    }
}







