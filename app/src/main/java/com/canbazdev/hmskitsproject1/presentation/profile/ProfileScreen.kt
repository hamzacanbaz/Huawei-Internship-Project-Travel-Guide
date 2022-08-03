package com.canbazdev.hmskitsproject1.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.canbazdev.hmskitsproject1.R
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post

/*
*   Created by hamzacanbaz on 8/1/2022
*/

@Composable
fun GetProfileScreen(profileViewModel: ProfileViewModel) {
    val userId by profileViewModel.userId.collectAsState()
    val userEmail by profileViewModel.userEmail.collectAsState()
    val userLandmarks by profileViewModel.landmarks.collectAsState()
    val userLandmarkss =
        listOf<Post>(
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1"),
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1"),
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1"),
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1"),
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1"),
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1"),
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1"),
            Post(landmarkImage = "https://firebasestorage.googleapis.com/v0/b/hmskitsproject1.appspot.com/o/images%2F3f66553f-e28c-442f-8436-ff5565c03d10?alt=media&token=a25bee3d-ba63-4833-a8f0-1b06a5472ea1")
        )

    var isLottiePlaying by remember { mutableStateOf(true) }
    var animationSpeed by remember { mutableStateOf(1f) }
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

                items(3 * (userLandmarks.size / 3) + 3) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(0.dp, 12.dp, 0.dp, 12.dp)
                    ) {
                        if (it < userLandmarks.size) {
                            userLandmarks[it].landmarkImage?.let { image ->
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








