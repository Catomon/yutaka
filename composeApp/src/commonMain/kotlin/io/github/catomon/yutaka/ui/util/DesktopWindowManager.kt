package io.github.catomon.yutaka.ui.util

class DesktopWindowManager(
    val minimize: () -> Boolean,
    val maximize: () -> Boolean,
    val closeWindow: () -> Unit
)