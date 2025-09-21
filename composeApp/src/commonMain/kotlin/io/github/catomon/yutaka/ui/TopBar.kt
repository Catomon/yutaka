package io.github.catomon.yutaka.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.catomon.yutaka.ui.local_providers.LocalWindowManager
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources.close_window
import yutaka.composeapp.generated.resources.full_window
import yutaka.composeapp.generated.resources.hide_window
import yutaka.composeapp.generated.resources.normal_window

object TopBarDefaults {
    const val HEIGHT = 80
}

@Composable
fun TopBar(
    viewModel: MainViewModel,
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMaximized by remember { mutableStateOf(false) }
    val windowManager = LocalWindowManager.current

    Column(modifier.fillMaxWidth().height(TopBarDefaults.HEIGHT.dp).background(color = MaterialTheme.colorScheme.surface)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Yutaka",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(-2259835),
                modifier = Modifier.clickable {
                    onOpenMenu()
                }
            )

            Row {
                IconButton(onClick = {
                    windowManager.minimize
                }) {
                    Icon(
                        painter = painterResource(Res.drawable.hide_window),
                        contentDescription = "Hide window",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                IconButton(onClick = {
                    isMaximized = windowManager.maximize()
                }) {
                    Icon(
                        painter = if (isMaximized) painterResource(Res.drawable.normal_window) else painterResource(Res.drawable.full_window),
                        contentDescription = "Toggle maximize",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                IconButton(onClick = windowManager.closeWindow) {
                    Icon(
                        painter = painterResource(Res.drawable.close_window),
                        contentDescription = "Close window",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        AnimatedContent(viewModel.viewPost, modifier.fillMaxWidth().height(48.dp)) {
            when (it) {
                null -> {
                    Row(verticalAlignment = Alignment.Companion.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Button(viewModel::refresh, enabled = !viewModel.isLoading) {
                            Text("Refresh")
                        }

                        Button({
                            viewModel.changePage(0)
                        }, enabled = viewModel.page > 0) {
                            Text("<Start")
                        }

                        Button({
                            viewModel.changePage(viewModel.page - 1)
                        }, enabled = viewModel.page > 0) {
                            Text("<Prev")
                        }

                        Button({
                            viewModel.changePage(viewModel.page + 1)
                        }) {
                            Text("Next>")
                        }

                        Text("Page ${viewModel.page + 1}")

                        Spacer(Modifier.Companion.weight(1f))

                        AnimatedContent(viewModel.status) {
                            when (viewModel.status) {
                                MainViewModel.Companion.STATUS_LOADING -> CircularProgressIndicator()
                                MainViewModel.Companion.STATUS_SUCCESS -> Text("OK", color = Color.Companion.Green)
                                MainViewModel.Companion.STATUS_FAIL -> Text("OTD", color = Color.Companion.Red)
                            }
                        }
                    }
                }

                else -> {
                    var isDownloading by remember { mutableStateOf(false) }
                    var state by remember { mutableStateOf("") }

                    Row(Modifier.fillMaxWidth()) {
                        Button(viewModel::closePost) {
                            Text("< Back")
                        }

                        Button(onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                val post = viewModel.viewPost ?: return@launch
                                isDownloading = true
                                state = ""
                                val result = downloadPost(post)
                                state = if (result) "Success." else "Fail."
                                isDownloading = false
                            }
                        }, enabled = !isDownloading) {
                            Text("Download")
                        }

                        Text(state)
                    }
                }
            }
        }
    }
}