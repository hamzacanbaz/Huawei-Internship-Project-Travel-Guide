package com.canbazdev.hmskitsproject1.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    val dayTime by profileViewModel.dayTime.collectAsState()
    val currentLandmarks by profileViewModel.currentLandmarks.collectAsState(profileViewModel.landmarks.value)
    var alertDialogOpened by remember { mutableStateOf(false) }
    var clickedLandmark by remember { mutableStateOf(Post()) }
    var selectedSharedOrToGo by remember { mutableStateOf(0) }

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
            AlertDialog(
                openDialog = alertDialogOpened,
                closeDialog = { alertDialogOpened = false },
                deleteItemAndCloseDialog = {
                    profileViewModel.removeFromWishList(clickedLandmark.id ?: " ")
                    profileViewModel.setWishListLandmarks()
                    alertDialogOpened = false
                },
                clickedLandmark,
                selectedSharedOrToGo
            )

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
                    UserText(text = dayTime)
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
                        selectedSharedOrToGo = it
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
                                GridImage(landmark = currentLandmarks[it], selectedLandmark = {
                                    if (selectedSharedOrToGo == 1) {
                                        alertDialogOpened = true
                                        clickedLandmark = it

                                    }
                                })
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
fun GridImage(landmark: Post, selectedLandmark: (Post) -> Unit) {
    com.skydoves.landscapist.glide.GlideImage(
        imageModel = landmark.landmarkImage,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clickable { selectedLandmark(landmark) }
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
    var selectedIndex by rememberSaveable {
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

@Composable
fun AlertDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    deleteItemAndCloseDialog: () -> Unit,
    landmark: Post,
    selectedSharedOrToGo: Int
) {
    Column {
        //val openDialog = remember { mutableStateOf(false) }


        if (openDialog) {

            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    closeDialog()
                },
                title = {
                    Text(text = "${landmark.landmarkName}", modifier = Modifier.fillMaxWidth())
                },
                text = {
                    Text("Are you sure want to delete this landmark? ")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            deleteItemAndCloseDialog()
                        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(

                        onClick = {
                            closeDialog()
                        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
    clear()
    addAll(newList)
}


@Preview(showBackground = true)
@Composable
fun MyPreview() {
    MaterialTheme {
        ShowImagesOrToDo(onChangeLandmarks = {})

    }
}







